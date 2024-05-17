package com.fiap.payments.adapter.gateway.impl

import com.fiap.payments.adapter.gateway.OrderGateway
import com.fiap.payments.client.OrderApiClient
import com.fiap.payments.domain.entities.Order

class OrderGatewayImpl(private val orderApiClient: OrderApiClient) : OrderGateway {

    override fun confirmOrder(orderNumber: Long): Order {
        return orderApiClient.confirm(orderNumber)
    }

}