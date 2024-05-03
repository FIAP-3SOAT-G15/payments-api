package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Component

interface SearchComponentUseCase {
    fun searchByName(componentName: String): List<Component>
}
