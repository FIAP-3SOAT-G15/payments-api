package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.usecases.ConfirmOrderUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.SyncPaymentUseCase
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class PaymentSyncService(
    private val loadPaymentUseCase: LoadPaymentUseCase,
    private val paymentGateway: PaymentGateway,
    private val paymentProviderGateway: PaymentProviderGateway,
    private val confirmOrderUseCase: ConfirmOrderUseCase
): SyncPaymentUseCase {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun syncPayment(orderNumber: Long, externalOrderGlobalId: String) {
        val payment = loadPaymentUseCase.getByOrderNumber(orderNumber)

        if (payment.externalOrderGlobalId == null) {
            paymentGateway.update(payment.copy(externalOrderGlobalId = externalOrderGlobalId))
        }

        val newStatus = paymentProviderGateway.checkExternalOrderStatus(externalOrderGlobalId)
        log.info("Checked payment status for order [$orderNumber]: $newStatus")

        if (payment.status != newStatus) {
            paymentGateway.update(
                payment.copy(
                    status = newStatus,
                    statusChangedAt = LocalDateTime.now(),
                )
            )
            log.info("Changed payment status for order [$orderNumber] from ${payment.status} to $newStatus")

            if (newStatus == PaymentStatus.CONFIRMED) {
                confirmOrderUseCase.confirmOrder(orderNumber)
            }
        }
    }
}
