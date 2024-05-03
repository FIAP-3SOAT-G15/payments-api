package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Component

interface CreateComponentUseCase {
    fun create(
        component: Component,
        initialQuantity: Long,
    ): Component
}
