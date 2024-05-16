package com.fiap.payments.driver.database.provider

import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.domain.entities.Order
import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.valueobjects.PaymentStatus
import java.util.*

class PaymentProviderGatewayMock: PaymentProviderGateway {

    override fun createExternalOrder(order: Order): PaymentRequest {
        return PaymentRequest(
            externalOrderId = UUID.randomUUID().toString(),
            externalOrderGlobalId = null,
            paymentInfo = "mocked"
        )
    }

    override fun checkExternalOrderStatus(externalOrderGlobalId: String): PaymentStatus {
        // always confirming
        return PaymentStatus.CONFIRMED
    }
}
