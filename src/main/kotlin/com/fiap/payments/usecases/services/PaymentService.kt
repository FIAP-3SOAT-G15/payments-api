package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.web.request.PaymentHTTPRequest
import com.fiap.payments.usecases.ChangePaymentStatusUseCase
import com.fiap.payments.usecases.ConfirmOrderUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.ProvidePaymentRequestUseCase
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class PaymentService(
    private val paymentGateway: PaymentGateway,
    private val paymentProvider: PaymentProviderGateway,
    private val confirmOrderUseCase: ConfirmOrderUseCase,
) : LoadPaymentUseCase,
    ProvidePaymentRequestUseCase,
    ChangePaymentStatusUseCase
{
    private val log = LoggerFactory.getLogger(javaClass)

    override fun getByPaymentId(paymentId: String): Payment =
        paymentGateway.findByPaymentId(paymentId)
            ?: throw PaymentsException(
                errorType = ErrorType.PAYMENT_NOT_FOUND,
                message = "Payment [$paymentId] not found",
            )

    override fun findByPaymentId(paymentId: String): Payment? = 
        paymentGateway.findByPaymentId(paymentId)

    override fun findAll(): List<Payment> =
        paymentGateway.findAll()

    override fun providePaymentRequest(paymentHTTPRequest: PaymentHTTPRequest): Payment {
        var payment = Payment(
            orderNumber = paymentHTTPRequest.orderInfo.number,
            createdAt = LocalDateTime.now(),
            status = PaymentStatus.PENDING,
            statusChangedAt = LocalDateTime.now(),
        )
        payment = paymentGateway.upsert(payment)
        log.info("Payment $payment stored for order [${payment.orderNumber}]")

        log.info("Requesting payment request for order [${paymentHTTPRequest.orderInfo.number}]")
        val paymentRequest = paymentProvider.createExternalOrder(payment.id, paymentHTTPRequest)

        payment = payment.copy(
            externalOrderId = paymentRequest.externalOrderId,
            externalOrderGlobalId = null,
            paymentInfo = paymentRequest.paymentInfo,
        )
        paymentGateway.upsert(payment)

        return payment
    }

    override fun confirmPayment(paymentId: String): Payment {
        val confirmedPayment = changePaymentStatus(paymentId = paymentId, newStatus = PaymentStatus.CONFIRMED)
        confirmOrderUseCase.confirmOrder(confirmedPayment.orderNumber)
        return confirmedPayment
    }

    override fun failPayment(paymentId: String): Payment =
        changePaymentStatus(paymentId = paymentId, newStatus = PaymentStatus.FAILED)

    override fun expirePayment(paymentId: String): Payment =
        changePaymentStatus(paymentId = paymentId, newStatus = PaymentStatus.EXPIRED)

    private fun changePaymentStatus(paymentId: String, newStatus: PaymentStatus): Payment =
        getByPaymentId(paymentId)
            .takeIf { it.status == PaymentStatus.PENDING }
            ?.let { payment ->
                log.info("Changing status of payment $payment to $newStatus")
                val changedPayment = paymentGateway.upsert(payment.copy(
                    status = newStatus,
                    statusChangedAt = LocalDateTime.now()
                ))
                changedPayment
            }
            ?: throw PaymentsException(
                errorType = ErrorType.INVALID_PAYMENT_STATE_TRANSITION,
                message = "Cannot change status of non-pending payment",
            )
}
