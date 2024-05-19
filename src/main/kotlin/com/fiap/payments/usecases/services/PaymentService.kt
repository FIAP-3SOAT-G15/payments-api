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

    override fun getByPaymentId(id: String): Payment {
        return paymentGateway.findByPaymentId(id)
            ?: throw PaymentsException(
                errorType = ErrorType.PAYMENT_NOT_FOUND,
                message = "Payment [$id] not found",
            )
    }

    override fun findByPaymentId(id: String): Payment? {
        return paymentGateway.findByPaymentId(id)
    }

    override fun findAll(): List<Payment> {
        return paymentGateway.findAll()
    }

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
        return getByPaymentId(paymentId)
            .takeIf { it.status == PaymentStatus.PENDING }
            ?.let { payment ->
                log.info("Confirming payment $payment")
                val confirmedPayment = paymentGateway.upsert(payment.copy(
                    status = PaymentStatus.CONFIRMED,
                    statusChangedAt = LocalDateTime.now()
                ))
                confirmOrderUseCase.confirmOrder(confirmedPayment.orderNumber)
                confirmedPayment
            }
            ?: throw PaymentsException(
                errorType = ErrorType.INVALID_PAYMENT_STATE_TRANSITION,
                message = "Payment can only be confirmed when it is pending",
            )
    }

    override fun failPayment(paymentId: String): Payment {
        return getByPaymentId(paymentId)
            .takeIf { it.status == PaymentStatus.PENDING }
            ?.let { payment ->
                log.info("Failing payment $payment")
                paymentGateway.upsert(payment.copy(
                    status = PaymentStatus.FAILED,
                    statusChangedAt = LocalDateTime.now()
                ))
            }
            ?: throw PaymentsException(
                errorType = ErrorType.INVALID_PAYMENT_STATE_TRANSITION,
                message = "Payment can only be failed when it is pending",
            )
    }

    override fun expirePayment(paymentId: String): Payment {
        return getByPaymentId(paymentId)
            .takeIf { it.status == PaymentStatus.PENDING }
            ?.let { payment ->
                log.info("Expiring payment $payment")
                paymentGateway.upsert(payment.copy(
                    status = PaymentStatus.EXPIRED,
                    statusChangedAt = LocalDateTime.now()
                ))
            }
            ?: throw PaymentsException(
                errorType = ErrorType.INVALID_PAYMENT_STATE_TRANSITION,
                message = "Payment can only be expired when it is pending",
            )
    }
}
