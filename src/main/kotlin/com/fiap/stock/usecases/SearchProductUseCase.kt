package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Product

interface SearchProductUseCase {
    fun searchByName(productName: String): List<Product>
}
