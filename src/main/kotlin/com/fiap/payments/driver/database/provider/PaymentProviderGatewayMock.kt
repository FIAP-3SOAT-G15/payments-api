package com.fiap.payments.driver.database.provider

import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.messaging.event.PaymentRequestEvent
import com.fiap.payments.driver.web.request.PaymentHTTPRequest
import org.slf4j.LoggerFactory
import java.util.*

class PaymentProviderGatewayMock: PaymentProviderGateway {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun createExternalOrder(paymentId: String, paymentHTTPRequest: PaymentRequestEvent): PaymentRequest {
        log.info("Providing mocked payment request for order [${paymentHTTPRequest.orderInfo.number}]")
        return PaymentRequest(
            externalOrderId = UUID.randomUUID().toString(),
            externalOrderGlobalId = null,
            paymentInfo = "00020101021243650016COM.MERCADOLIBRE..."
        )
    }

    override fun checkExternalOrderStatus(externalOrderGlobalId: String): PaymentStatus {
        // always confirming
        log.info("Returning confirmed payment request for order [$externalOrderGlobalId]")
        return PaymentStatus.CONFIRMED
    }
}
