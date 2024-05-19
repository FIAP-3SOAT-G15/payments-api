package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.usecases.ChangePaymentStatusUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.SyncPaymentUseCase
import org.slf4j.LoggerFactory

class PaymentSyncService(
    private val loadPaymentUseCase: LoadPaymentUseCase,
    private val paymentGateway: PaymentGateway,
    private val paymentProviderGateway: PaymentProviderGateway,
    private val changePaymentStatusUseCase: ChangePaymentStatusUseCase,
): SyncPaymentUseCase {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun syncPayment(paymentId: String, externalOrderGlobalId: String) {
        val payment = loadPaymentUseCase.getByPaymentId(paymentId)

        if (payment.externalOrderGlobalId == null) {
            paymentGateway.upsert(payment.copy(externalOrderGlobalId = externalOrderGlobalId))
        }

        val newStatus = paymentProviderGateway.checkExternalOrderStatus(externalOrderGlobalId)
        log.info("Checked payment status for payment [$paymentId]: $newStatus")

        if (payment.status != newStatus) {
            when (newStatus) {
                PaymentStatus.CONFIRMED -> changePaymentStatusUseCase.confirmPayment(paymentId)
                PaymentStatus.EXPIRED -> changePaymentStatusUseCase.expirePayment(paymentId)
                PaymentStatus.FAILED -> changePaymentStatusUseCase.failPayment(paymentId)
                PaymentStatus.PENDING -> throw PaymentsException(
                    errorType = ErrorType.INVALID_PAYMENT_STATE_TRANSITION,
                    message = "Payment cannot be transitioned to pending state again"
                )
            }
        }
    }
}
