package com.fiap.payments.adapter.gateway.impl

import com.fiap.payments.adapter.gateway.OrderGateway
import com.fiap.payments.client.OrderApiClient

class OrderGatewayImpl(
    private val orderApiClient: OrderApiClient
) : OrderGateway {

    override fun confirmOrder(orderNumber: Long) {
        return orderApiClient.confirm(orderNumber)
    }
}
