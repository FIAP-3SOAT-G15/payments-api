package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Product

interface RemoveProductUseCase {
    fun delete(productNumber: Long): Product
}
