package com.fiap.stock.driver.database.persistence.mapper

import com.fiap.stock.domain.entities.Stock
import com.fiap.stock.driver.database.persistence.entities.StockEntity
import org.mapstruct.Mapper

@Mapper
interface StockMapper {
    fun toDomain(entity: StockEntity): Stock

    fun toEntity(domain: Stock): StockEntity
}
