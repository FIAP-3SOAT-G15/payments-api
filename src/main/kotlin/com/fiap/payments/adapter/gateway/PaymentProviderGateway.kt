package com.fiap.payments.adapter.gateway

import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.messaging.event.PaymentRequestEvent

interface PaymentProviderGateway {
    fun createExternalOrder(paymentId: String, paymentHTTPRequest: PaymentRequestEvent): PaymentRequest

    fun checkExternalOrderStatus(externalOrderGlobalId: String): PaymentStatus
}
