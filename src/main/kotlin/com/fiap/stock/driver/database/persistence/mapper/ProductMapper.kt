package com.fiap.stock.driver.database.persistence.mapper

import com.fiap.stock.domain.entities.Product
import com.fiap.stock.driver.database.persistence.entities.ProductEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper
interface ProductMapper {
    @Mapping(
        source = "subItems",
        target = "subItems",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT,
    )
    fun toDomain(entity: ProductEntity): Product

    @Mapping(
        source = "subItems",
        target = "subItems",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    )
    fun toEntity(domain: Product): ProductEntity
}
