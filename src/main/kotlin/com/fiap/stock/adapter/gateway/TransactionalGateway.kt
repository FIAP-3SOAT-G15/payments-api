package com.fiap.stock.adapter.gateway

interface TransactionalGateway {
    fun <T> transaction(code: () -> T): T
}
