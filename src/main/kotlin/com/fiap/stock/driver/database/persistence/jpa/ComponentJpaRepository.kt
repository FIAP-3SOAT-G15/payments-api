package com.fiap.stock.driver.database.persistence.jpa

import com.fiap.stock.driver.database.persistence.entities.ComponentEntity
import org.springframework.data.repository.CrudRepository

interface ComponentJpaRepository : CrudRepository<ComponentEntity, Long> {
    fun findByNameContainingIgnoreCase(componentName: String): List<ComponentEntity>
}
