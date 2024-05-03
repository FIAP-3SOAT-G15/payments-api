package com.fiap.stock.adapter.gateway

import com.fiap.stock.domain.entities.Product
import com.fiap.stock.domain.valueobjects.ProductCategory

interface ProductGateway {
    fun findAll(): List<Product>

    fun findByProductNumber(productNumber: Long): Product?

    fun findByCategory(category: ProductCategory): List<Product>

    fun searchByName(name: String): List<Product>

    fun create(product: Product): Product

    fun update(product: Product): Product

    fun delete(productNumber: Long): Product

    fun deleteAll()
}
