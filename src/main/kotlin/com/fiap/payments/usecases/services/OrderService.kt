package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.OrderGateway
import com.fiap.payments.domain.entities.Order
import com.fiap.payments.usecases.ConfirmOrderUseCase

open class OrderService(
    private val orderGateway: OrderGateway,
) :
    ConfirmOrderUseCase
 {

    override fun confirmOrder(orderNumber: Long): Order {
        return orderGateway.confirmOrder(orderNumber)
    }




}
