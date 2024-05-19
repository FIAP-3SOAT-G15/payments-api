package com.fiap.payments

import com.fiap.payments.client.MercadoPagoMerchantOrderResponse
import com.fiap.payments.client.MercadoPagoQRCodeOrderRequest
import com.fiap.payments.client.MercadoPagoQRCodeOrderRequestItem
import com.fiap.payments.client.MercadoPagoQRCodeOrderResponse
import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.database.persistence.entities.PaymentDocument
import com.fiap.payments.driver.database.provider.MercadoPagoPaymentProvider
import com.fiap.payments.driver.web.request.OrderInfo
import com.fiap.payments.driver.web.request.OrderLine
import com.fiap.payments.driver.web.request.PaymentHTTPRequest
import java.math.BigDecimal
import java.time.LocalDateTime

fun createPaymentHTTPRequest(
    orderInfo: OrderInfo = createOrderInfo()
) = PaymentHTTPRequest(
    orderInfo = orderInfo
)

fun createOrderInfo(
    number: Long = 1,
    orderedAt: LocalDateTime = LocalDateTime.parse("2023-10-01T18:00:00"),
    orderedBy: String = "John Doe",
    totalAmount: BigDecimal = BigDecimal.valueOf(10)
) = OrderInfo(
    number = number,
    orderedAt = orderedAt,
    orderedBy = orderedBy,
    totalAmount = totalAmount,
    lines = listOf(
        OrderLine(
            name = "Item 1",
            quantity = 1,
            totalAmount = BigDecimal.valueOf(10),
            unitOfMeasurement = "unit",
            unitPrice = BigDecimal.valueOf(10),
        )
    )
)

fun createPayment(
    id: String = "445e11cd-c9fa-44ee-80e9-31a6cb1c7339",
    orderNumber: Long = 1,
    externalOrderId: String = "66b0f5f7-9997-4f49-a203-3dab2d936b50",
    externalOrderGlobalId: String? = null,
    paymentInfo: String = "00020101021243650016COM.MERCADOLIBRE...",
    createdAt: LocalDateTime = LocalDateTime.parse("2023-10-01T18:01:00"),
    status: PaymentStatus = PaymentStatus.PENDING,
    statusChangedAt: LocalDateTime = LocalDateTime.parse("2023-10-01T18:01:00"),
) = Payment(
    id = id,
    orderNumber = orderNumber,
    externalOrderId = externalOrderId,
    externalOrderGlobalId = externalOrderGlobalId,
    paymentInfo = paymentInfo,
    createdAt = createdAt,
    status = status,
    statusChangedAt = statusChangedAt,
)

fun Payment.toDocument() = PaymentDocument(
    id = id,
    orderNumber = orderNumber.toString(),
    externalOrderId = externalOrderId,
    externalOrderGlobalId = externalOrderGlobalId,
    paymentInfo = paymentInfo,
    createdAt = createdAt,
    status = status.name,
    statusChangedAt = statusChangedAt
)

fun createPaymentRequest(
    externalOrderId: String = "66b0f5f7-9997-4f49-a203-3dab2d936b50",
    externalOrderGlobalId: String? = null,
    paymentInfo: String = "00020101021243650016COM.MERCADOLIBRE...",
) = PaymentRequest(
    externalOrderId = externalOrderId,
    externalOrderGlobalId = externalOrderGlobalId,
    paymentInfo = paymentInfo,
)

fun createPaymentDocument(
    id: String = "9e9deccc-9194-4205-8c9f-154799ff0d69",
    orderNumber: String = "1",
    externalOrderId: String? = "66b0f5f7-9997-4f49-a203-3dab2d936b50",
    externalOrderGlobalId: String? = null,
    paymentInfo: String? = "00020101021243650016COM.MERCADOLIBRE...",
    createdAt: LocalDateTime = LocalDateTime.parse("2023-10-01T18:00:00"),
    status: String = PaymentStatus.PENDING.name,
    statusChangedAt: LocalDateTime = LocalDateTime.parse("2023-10-01T18:01:00"),
) = PaymentDocument(
    id = id,
    orderNumber = orderNumber,
    externalOrderId = externalOrderId,
    externalOrderGlobalId = externalOrderGlobalId,
    paymentInfo = paymentInfo,
    createdAt = createdAt,
    status = status,
    statusChangedAt = statusChangedAt,
)

fun PaymentDocument.toPaymentEntity() = Payment(
    id = id,
    orderNumber = orderNumber.toLong(),
    externalOrderId = externalOrderId,
    externalOrderGlobalId = externalOrderGlobalId,
    paymentInfo = paymentInfo,
    createdAt = createdAt!!,
    status = PaymentStatus.valueOf(status),
    statusChangedAt = statusChangedAt!!,
)

fun createMercadoPagoQRCodeOrderRequest(
    title: String = "Order title",
    description: String = "Order description",
    externalReference: String = "Order external reference",
    notificationUrl: String = "http://notify",
    totalAmount: BigDecimal = BigDecimal.valueOf(10)
) = MercadoPagoQRCodeOrderRequest(
    title = title,
    description = description,
    externalReference = externalReference,
    items = listOf(createMercadoPagoQRCodeOrderRequestItem()),
    notificationUrl = notificationUrl,
    totalAmount = totalAmount,
)

fun createMercadoPagoQRCodeOrderRequestItem(
    title: String = "Order item",
    unitPrice: BigDecimal = BigDecimal.valueOf(10),
    quantity: Long = 1,
    totalAmount: BigDecimal = BigDecimal.valueOf(10),
    unitMeasure: String = "unit"
) = MercadoPagoQRCodeOrderRequestItem(
    title = title,
    unitPrice = unitPrice,
    quantity = quantity,
    totalAmount = totalAmount,
    unitMeasure = unitMeasure
)

fun createMercadoPagoQRCodeOrderResponse(
    qrData: String = "00020101021243650016COM.MERCADOLIBRE...",
    inStoreOrderId: String = "1"
) = MercadoPagoQRCodeOrderResponse(
    qrData = qrData,
    inStoreOrderId = inStoreOrderId,
)

fun createMercadoPagoMerchantOrderResponse(
    id: String = "1",
    externalReference: String = "ref",
    orderStatus: String = MercadoPagoPaymentProvider.MercadoPagoOrderStatus.PAID.name,
) = MercadoPagoMerchantOrderResponse(
    id = id,
    externalReference = externalReference,
    orderStatus = orderStatus,
)
