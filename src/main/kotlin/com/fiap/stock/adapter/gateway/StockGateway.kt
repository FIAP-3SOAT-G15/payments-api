package com.fiap.stock.adapter.gateway

import com.fiap.stock.domain.entities.Stock

interface StockGateway {
    fun findByComponentNumber(componentNumber: Long): Stock?

    fun create(stock: Stock): Stock

    fun update(stock: Stock): Stock
}
