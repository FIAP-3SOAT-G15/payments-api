package com.fiap.stock.adapter.gateway

import com.fiap.stock.domain.entities.Component

interface ComponentGateway {
    fun findAll(): List<Component>

    fun findByComponentNumber(componentNumber: Long): Component?

    fun searchByName(componentName: String): List<Component>

    fun create(component: Component): Component

    fun update(component: Component): Component

    fun delete(component: Component): Component

    fun deleteAll()
}
