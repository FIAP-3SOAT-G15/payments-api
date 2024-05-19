package com.fiap.payments.adapter.gateway

interface OrderGateway {
    fun confirmOrder(orderNumber: Long)
}
