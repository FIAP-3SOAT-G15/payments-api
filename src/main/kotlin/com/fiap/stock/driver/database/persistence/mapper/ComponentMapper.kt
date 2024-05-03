package com.fiap.stock.driver.database.persistence.mapper

import com.fiap.stock.domain.entities.Component
import com.fiap.stock.driver.database.persistence.entities.ComponentEntity
import org.mapstruct.Mapper

@Mapper
interface ComponentMapper {
    fun toDomain(entity: ComponentEntity): Component

    fun toEntity(domain: Component): ComponentEntity
}
