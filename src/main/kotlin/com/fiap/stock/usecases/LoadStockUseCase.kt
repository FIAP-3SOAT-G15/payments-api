package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Stock

interface LoadStockUseCase {
    fun getByComponentNumber(componentNumber: Long): Stock
}
