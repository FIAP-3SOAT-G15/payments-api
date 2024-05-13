package com.fiap.payments.adapter.gateway

import com.fiap.payments.domain.entities.Order
import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.valueobjects.PaymentStatus


interface PaymentProviderGateway {
    fun createExternalOrder(order: Order): PaymentRequest

    fun checkExternalOrderStatus(externalOrderGlobalId: String): PaymentStatus
}
