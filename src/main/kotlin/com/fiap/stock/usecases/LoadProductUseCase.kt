package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Product
import com.fiap.stock.domain.valueobjects.ProductCategory

interface LoadProductUseCase {
    fun getByProductNumber(productNumber: Long): Product

    fun findAll(): List<Product>

    fun findByCategory(category: ProductCategory): List<Product>
}
