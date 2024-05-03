package com.fiap.stock.adapter.controller

import com.fiap.stock.domain.entities.Product
import com.fiap.stock.domain.valueobjects.ProductCategory
import com.fiap.stock.driver.web.MenuAPI
import com.fiap.stock.usecases.LoadProductUseCase
import com.fiap.stock.usecases.SearchProductUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController(
    private val loadProductUseCase: LoadProductUseCase,
    private val searchProductUseCase: SearchProductUseCase,
) : MenuAPI {
    override fun findAll(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(loadProductUseCase.findAll())
    }

    override fun findByCategory(category: String): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(loadProductUseCase.findByCategory(ProductCategory.fromString(category)))
    }

    override fun searchByName(name: String): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(searchProductUseCase.searchByName(name))
    }
}
