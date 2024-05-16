package com.fiap.payments.adapter.gateway

interface TransactionalGateway {
    fun <T> transaction(code: () -> T): T
}
