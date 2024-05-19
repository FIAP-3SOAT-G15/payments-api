package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.domain.entities.Order
import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.domain.entities.PaymentRequest
import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.ProvidePaymentRequestUseCase
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class PaymentService(
    private val paymentRepository: PaymentGateway,
    private val paymentProvider: PaymentProviderGateway,
) : LoadPaymentUseCase,
    ProvidePaymentRequestUseCase
{
    private val log = LoggerFactory.getLogger(javaClass)
    
    override fun getByOrderNumber(orderNumber: Long): Payment {
        return paymentRepository.findByOrderNumber(orderNumber)
            ?: throw PaymentsException(
                errorType = ErrorType.PAYMENT_NOT_FOUND,
                message = "Payment not found for order [$orderNumber]",
            )
    }

    override fun findByOrderNumber(orderNumber: Long): Payment? {
        return paymentRepository.findByOrderNumber(orderNumber)
    }

    override fun findAll(): List<Payment> {
        return paymentRepository.findAll()
    }

    override fun providePaymentRequest(order: Order): PaymentRequest {
        val paymentRequest = paymentProvider.createExternalOrder(order)
        log.info("Payment request created for order $order")

        val payment =
            Payment(
                orderNumber = order.number!!,
                externalOrderId = paymentRequest.externalOrderId,
                externalOrderGlobalId = null,
                paymentInfo = paymentRequest.paymentInfo,
                createdAt = LocalDateTime.now(),
                status = PaymentStatus.PENDING,
                statusChangedAt = LocalDateTime.now(),
            )

        paymentRepository.create(payment)
        log.info("Payment stored for order $order")

        return paymentRequest
    }
}
