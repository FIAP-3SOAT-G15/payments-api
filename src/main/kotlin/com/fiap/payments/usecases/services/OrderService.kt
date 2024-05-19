package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.OrderGateway
import com.fiap.payments.usecases.ConfirmOrderUseCase
import org.slf4j.LoggerFactory

open class OrderService(
    private val orderGateway: OrderGateway,
): ConfirmOrderUseCase {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun confirmOrder(orderNumber: Long) {
        log.info("Requesting order [$orderNumber] to be confirmed")
        return orderGateway.confirmOrder(orderNumber)
    }
}
