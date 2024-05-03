package com.fiap.stock.usecases.services

import com.fiap.stock.adapter.gateway.ComponentGateway
import com.fiap.stock.adapter.gateway.ProductGateway
import com.fiap.stock.adapter.gateway.StockGateway
import com.fiap.stock.domain.entities.Component
import com.fiap.stock.domain.entities.Stock
import com.fiap.stock.domain.errors.ErrorType
import com.fiap.stock.domain.errors.SelfOrderManagementException
import com.fiap.stock.usecases.CreateComponentUseCase
import com.fiap.stock.usecases.LoadComponentUseCase
import com.fiap.stock.usecases.SearchComponentUseCase

class ComponentService(
    private val componentRepository: ComponentGateway,
    private val stockRepository: StockGateway,
    private val productRepository: ProductGateway,
) : LoadComponentUseCase,
    SearchComponentUseCase,
    CreateComponentUseCase {
    override fun getByComponentNumber(componentNumber: Long): Component {
        return componentRepository.findByComponentNumber(componentNumber)
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.COMPONENT_NOT_FOUND,
                message = "Component [$componentNumber] not found",
            )
    }

    override fun findByProductNumber(productNumber: Long): List<Component> {
        return productRepository.findByProductNumber(productNumber)
            ?.components
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.PRODUCT_NOT_FOUND,
                message = "Product [$productNumber] not found",
            )
    }

    override fun searchByName(componentName: String): List<Component> {
        return componentRepository.searchByName(componentName)
    }

    override fun findAll(): List<Component> {
        return componentRepository.findAll()
    }

    override fun create(
        component: Component,
        initialQuantity: Long,
    ): Component {
        val savedComponent = componentRepository.create(component)
        val stock = Stock(componentNumber = savedComponent.number!!, quantity = initialQuantity)
        stockRepository.create(stock)
        return savedComponent
    }
}
