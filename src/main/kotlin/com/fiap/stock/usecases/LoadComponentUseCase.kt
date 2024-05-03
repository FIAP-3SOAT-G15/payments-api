package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Component

interface LoadComponentUseCase {
    fun getByComponentNumber(componentNumber: Long): Component

    fun findByProductNumber(productNumber: Long): List<Component>

    fun findAll(): List<Component>
}
