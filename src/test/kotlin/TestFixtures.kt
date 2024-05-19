import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.web.request.OrderInfo
import com.fiap.payments.driver.web.request.OrderLine
import com.fiap.payments.driver.web.request.PaymentHTTPRequest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

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

fun createPaymentRequest(
    externalOrderId: String = "66b0f5f7-9997-4f49-a203-3dab2d936b50",
    externalOrderGlobalId: String? = null,
    paymentInfo: String = "00020101021243650016COM.MERCADOLIBRE...",
) = PaymentRequest(
    externalOrderId = externalOrderId,
    externalOrderGlobalId = externalOrderGlobalId,
    paymentInfo = paymentInfo,
)
