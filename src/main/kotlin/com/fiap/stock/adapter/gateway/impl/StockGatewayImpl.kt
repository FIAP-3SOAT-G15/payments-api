package com.fiap.stock.adapter.gateway.impl

import com.fiap.stock.adapter.gateway.StockGateway
import com.fiap.stock.domain.entities.Stock
import com.fiap.stock.domain.errors.ErrorType
import com.fiap.stock.domain.errors.SelfOrderManagementException
import com.fiap.stock.driver.database.persistence.jpa.StockJpaRepository
import com.fiap.stock.driver.database.persistence.mapper.StockMapper
import org.mapstruct.factory.Mappers

class StockGatewayImpl(
    private val stockJpaRepository: StockJpaRepository,
) : StockGateway {
    private val mapper = Mappers.getMapper(StockMapper::class.java)

    override fun findByComponentNumber(componentNumber: Long): Stock? {
        return stockJpaRepository.findById(componentNumber)
            .map(mapper::toDomain)
            .orElse(null)
    }

    override fun create(stock: Stock): Stock {
        findByComponentNumber(stock.componentNumber)?.let {
            throw SelfOrderManagementException(
                errorType = ErrorType.STOCK_ALREADY_EXISTS,
                message = "Stock record for component [${stock.componentNumber}] already exists",
            )
        }
        return persist(stock)
    }

    override fun update(stock: Stock): Stock {
        val newItem =
            findByComponentNumber(stock.componentNumber)
                ?.update(stock)
                ?: throw SelfOrderManagementException(
                    errorType = ErrorType.STOCK_NOT_FOUND,
                    message = "Stock [${stock.componentNumber}] not found",
                )
        return persist(newItem)
    }

    private fun persist(stock: Stock): Stock =
        stock
            .let(mapper::toEntity)
            .let(stockJpaRepository::save)
            .let(mapper::toDomain)
}
