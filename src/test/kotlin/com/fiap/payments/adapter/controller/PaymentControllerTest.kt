package com.fiap.payments.adapter.controller

import com.fiap.payments.adapter.controller.PaymentController.IPNType
import com.fiap.payments.createPayment
import com.fiap.payments.createPaymentEvent
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.usecases.ChangePaymentStatusUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.ProvidePaymentRequestUseCase
import com.fiap.payments.usecases.SyncPaymentUseCase
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.http.HttpStatus
import java.util.*

class PaymentControllerTest {
    private val loadPaymentUseCase = mockk<LoadPaymentUseCase>()
    private val syncPaymentUseCase = mockk<SyncPaymentUseCase>()
    private val changePaymentStatusUseCase = mockk<ChangePaymentStatusUseCase>()

    private val paymentController =
        PaymentController(
            loadPaymentUseCase,
            syncPaymentUseCase,
            changePaymentStatusUseCase
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should find all payments`() {
        val payments = listOf(createPayment())
        every { loadPaymentUseCase.findAll() } returns payments

        val result = paymentController.findAll()

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).containsExactlyInAnyOrderElementsOf(payments)
    }

    @Test
    fun `should get specific payment`() {
        val paymentId = UUID.randomUUID().toString()
        val payment = createPayment()
        every { loadPaymentUseCase.getByPaymentId(paymentId) } returns payment

        val result = paymentController.getByPaymentId(paymentId)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(payment)
    }
    

    @Test
    fun `should fail payment`() {
        val paymentId = UUID.randomUUID().toString()
        val payment = createPayment(status = PaymentStatus.FAILED)
        every { changePaymentStatusUseCase.failPayment(paymentId) } returns payment

        val result = paymentController.fail(paymentId)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(payment)
    }

    @Test
    fun `should expire payment`() {
        val paymentId = UUID.randomUUID().toString()
        val payment = createPayment(status = PaymentStatus.EXPIRED)
        every { changePaymentStatusUseCase.expirePayment(paymentId) } returns payment

        val result = paymentController.expire(paymentId)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(payment)
    }

    @Test
    fun `should confirm payment`() {
        val paymentId = UUID.randomUUID().toString()
        val payment = createPayment(status = PaymentStatus.CONFIRMED)
        every { changePaymentStatusUseCase.confirmPayment(paymentId) } returns payment

        val result = paymentController.confirm(paymentId)

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo(payment)
    }

    @Nested
    inner class Notify {

        @Test
        fun `should sync internal payment after receiving merchant order notification`() {
            val paymentId = UUID.randomUUID().toString()
            val resourceId = "externalId"
            val topic = IPNType.MERCHANT_ORDER.ipnType
            justRun { syncPaymentUseCase.syncPayment(paymentId, resourceId) }

            val result = paymentController.notify(paymentId, resourceId, topic)

            verify { syncPaymentUseCase.syncPayment(paymentId, resourceId) }
            assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        }

        @Test
        fun `should sync internal payment after receiving notification for payment with external order global ID`() {
            val paymentId = UUID.randomUUID().toString()
            val resourceId = "externalId"
            val topic = IPNType.PAYMENT.ipnType
            val payment = createPayment(id = paymentId, externalOrderGlobalId = resourceId)

            justRun { syncPaymentUseCase.syncPayment(paymentId, resourceId) }
            every { loadPaymentUseCase.getByPaymentId(paymentId) } returns payment

            val result = paymentController.notify(paymentId, resourceId, topic)

            verify { syncPaymentUseCase.syncPayment(paymentId, resourceId) }
            assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        }

        @Test
        fun `should throw error after receiving payment notification without external order global ID`() {
            val paymentId = UUID.randomUUID().toString()
            val resourceId = "externalId"
            val topic = IPNType.PAYMENT.ipnType
            val payment = createPayment(id = paymentId, externalOrderGlobalId = null)

            every { loadPaymentUseCase.getByPaymentId(paymentId) } returns payment

            val result = paymentController.notify(paymentId, resourceId, topic)

            verify(exactly = 0) { syncPaymentUseCase.syncPayment(any(), any()) }
            assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        @ParameterizedTest
        @EnumSource(IPNType::class, names = ["CHARGEBACK", "POINT_INTEGRATION_IPN"])
        fun `should throw error after receiving unsupported notification type`(topic: IPNType) {
            val paymentId = UUID.randomUUID().toString()
            val resourceId = "externalId"

            val result = paymentController.notify(paymentId, resourceId, topic.ipnType)

            verify(exactly = 0) { syncPaymentUseCase.syncPayment(any(), any()) }
            assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        }
    }
}
