package com.fiap.payments.adapter.controller

import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.driver.web.PaymentAPI
import com.fiap.payments.usecases.ChangePaymentStatusUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.SyncPaymentUseCase
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val loadPaymentUseCase: LoadPaymentUseCase,
    private val syncPaymentUseCase: SyncPaymentUseCase,
    private val changePaymentStatusUseCase: ChangePaymentStatusUseCase,
) : PaymentAPI {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun findAll(): ResponseEntity<List<Payment>> {
        return ResponseEntity.ok(loadPaymentUseCase.findAll())
    }

    override fun getByPaymentId(paymentId: String): ResponseEntity<Payment> {
        return ResponseEntity.ok(loadPaymentUseCase.getByPaymentId(paymentId))
    }


    override fun fail(paymentId: String): ResponseEntity<Payment> {
        return ResponseEntity.ok(changePaymentStatusUseCase.failPayment(paymentId))
    }

    override fun expire(paymentId: String): ResponseEntity<Payment> {
        return ResponseEntity.ok(changePaymentStatusUseCase.expirePayment(paymentId))
    }

    override fun confirm(paymentId: String): ResponseEntity<Payment> {
        return ResponseEntity.ok(changePaymentStatusUseCase.confirmPayment(paymentId))
    }

    /**
     * The server response is important to flag the provider for retries
     */
    override fun notify(paymentId: String, resourceId: String, topic: String): ResponseEntity<Any> {
        // TODO: verify x-signature header allowing only request from Mercado Pago
        log.info("Notification received for payment [${paymentId}]: type=${topic} externalId=${resourceId}")

        when (topic) {
            IPNType.MERCHANT_ORDER.ipnType -> {
                syncPaymentUseCase.syncPayment(paymentId, resourceId)
                return ResponseEntity.ok().build()
            }
            IPNType.PAYMENT.ipnType -> {
                val payment = loadPaymentUseCase.getByPaymentId(paymentId)
                payment.externalOrderGlobalId?.let {
                    syncPaymentUseCase.syncPayment(paymentId, it)
                    return ResponseEntity.ok().build()
                }
                // returns server error because external order global ID was not previously saved,
                // which does not conform with the usual application flow
                return ResponseEntity.internalServerError().build()
            }
            else -> {
                // returns bad request because application does not accept this IPN type
                return ResponseEntity.badRequest().build()
            }
        }
    }

    enum class IPNType(val ipnType: String) {
        MERCHANT_ORDER("merchant_order"),
        PAYMENT("payment"),
        CHARGEBACK("chargebacks"),
        POINT_INTEGRATION_IPN("point_integration_ipn"),
    }
}
