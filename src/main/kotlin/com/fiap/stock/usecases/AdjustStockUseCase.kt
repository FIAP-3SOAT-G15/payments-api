package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Stock

interface AdjustStockUseCase {
    fun increment(
        componentNumber: Long,
        quantity: Long,
    ): Stock

    fun decrement(
        componentNumber: Long,
        quantity: Long,
    ): Stock
}
