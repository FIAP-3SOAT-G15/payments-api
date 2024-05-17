package com.fiap.payments.adapter.gateway

import com.fiap.payments.domain.entities.Order

interface OrderGateway {
    fun confirmOrder(orderNumber: Long): Order
}