package com.fiap.payments.driver.database.provider

import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.client.MercadoPagoClient
import com.fiap.payments.client.MercadoPagoQRCodeOrderRequest
import com.fiap.payments.client.MercadoPagoQRCodeOrderRequestItem
import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.web.request.PaymentHTTPRequest

class MercadoPagoPaymentProvider(
    private val mercadoPagoClient: MercadoPagoClient,
    private val webhookBaseUrl: String,
) : PaymentProviderGateway {
    
    override fun createExternalOrder(paymentId: String, paymentHTTPRequest: PaymentHTTPRequest): PaymentRequest {
        // source_news=ipn indicates application will receive only Instant Payment Notifications (IPNs), not webhooks
        val notificationUrl = "${webhookBaseUrl}/payments/notifications/${paymentId}?source_news=ipn"

        val response =
            mercadoPagoClient.submitMerchantOrder(
                MercadoPagoQRCodeOrderRequest(
                    title = "Order ${paymentHTTPRequest.orderInfo.number}",
                    description = "Ordered at ${paymentHTTPRequest.orderInfo.orderedAt} " +
                        "by ${ paymentHTTPRequest.orderInfo.orderedBy }",
                    externalReference = paymentHTTPRequest.orderInfo.number.toString(),
                    notificationUrl = notificationUrl,
                    totalAmount = paymentHTTPRequest.orderInfo.totalAmount,
                    items = paymentHTTPRequest.orderInfo.lines.map { orderLine ->
                            MercadoPagoQRCodeOrderRequestItem(
                                title = orderLine.name,
                                unitPrice = orderLine.unitPrice,
                                quantity = orderLine.quantity,
                                unitMeasure = orderLine.unitOfMeasurement,
                                totalAmount = orderLine.totalAmount,
                            )
                        },
                ),
            )

        return PaymentRequest(
            externalOrderId = response.inStoreOrderId,
            externalOrderGlobalId = null,
            paymentInfo = response.qrData,
        )
    }

    override fun checkExternalOrderStatus(externalOrderGlobalId: String): PaymentStatus {
        val response = mercadoPagoClient.fetchMerchantOrder(externalOrderGlobalId)

        return when (response.orderStatus) {
            MercadoPagoOrderStatus.PAID.orderStatus -> {
                PaymentStatus.CONFIRMED
            }
            MercadoPagoOrderStatus.EXPIRED.orderStatus -> {
                PaymentStatus.EXPIRED
            }
            MercadoPagoOrderStatus.PAYMENT_IN_PROCESS.orderStatus,
            MercadoPagoOrderStatus.PAYMENT_REQUIRED.orderStatus -> {
                PaymentStatus.PENDING
            }
            else -> {
                PaymentStatus.FAILED
            }
        }
    }

    /**
     * Not exhaustive list of order statuses.
     */
    enum class MercadoPagoOrderStatus(val orderStatus: String) {
        PAID("paid"),
        EXPIRED("expired"),
        PAYMENT_IN_PROCESS("payment_in_process"),
        PAYMENT_REQUIRED("payment_required"),
    }
}
