package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.createPayment
import com.fiap.payments.createPaymentHTTPRequest
import com.fiap.payments.createPaymentRequest
import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.usecases.ConfirmOrderUseCase
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.LocalDateTime
import java.util.*

class PaymentServiceTest {
    private val paymentRepository = mockk<PaymentGateway>()
    private val paymentProvider = mockk<PaymentProviderGateway>()
    private val confirmOrderUseCase = mockk<ConfirmOrderUseCase>()

    private val paymentService =
        PaymentService(
            paymentRepository,
            paymentProvider,
            confirmOrderUseCase
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Nested
    inner class GetByPaymentId {
        @Test
        fun `should return existent payment`() {
            val payment = createPayment()
            every { paymentRepository.findByPaymentId(payment.id) } returns payment
            val result = paymentService.getByPaymentId(payment.id)
            assertThat(result).isEqualTo(payment)
        }

        @Test
        fun `should throw an exception when payment is not found`() {
            val paymentId = "5019af79-11c8-4100-9d3c-e98563b1c52c"

            every { paymentRepository.findByPaymentId(paymentId) } returns null

            assertThatThrownBy { paymentService.getByPaymentId(paymentId) }
                .isInstanceOf(PaymentsException::class.java)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.PAYMENT_NOT_FOUND)
        }
    }
    
    @Nested
    inner class FindByPaymentId {
        @Test
        fun `should find payment`() {
            val paymentId = UUID.randomUUID().toString()
            val payment = createPayment(id = paymentId)
            
            every { paymentRepository.findByPaymentId(paymentId) } returns payment
            
            val result = paymentService.findByPaymentId(paymentId)
            
            assertThat(result).isEqualTo(payment)
            verify(exactly = 1) { paymentRepository.findByPaymentId(paymentId) }
        }
    }

    @Nested
    inner class FindAll {
        @Test
        fun `should find all payments`() {
            val payments = listOf(createPayment())
            
            every { paymentRepository.findAll() } returns payments
            
            val result = paymentService.findAll()
            
            assertThat(result).containsExactlyElementsOf(payments)
            verify(exactly = 1) { paymentRepository.findAll() }
        }
    }

    @Nested
    inner class ProvidePaymentRequest {

        @Test
        fun `should create payment`() {
            val paymentHTTPRequest = createPaymentHTTPRequest()
            val payment = createPayment()
            val paymentRequest = createPaymentRequest()

            every { paymentRepository.upsert(any()) } returns payment
            every { paymentProvider.createExternalOrder(payment.id, paymentHTTPRequest) } returns paymentRequest

            val result = paymentService.providePaymentRequest(paymentHTTPRequest)

            assertThat(result).isEqualTo(payment)
            verify(exactly = 2) { paymentRepository.upsert(any()) }
        }
    }
    
    @Nested
    inner class ConfirmPayment {
        @Test
        fun `should confirm payment when state is pending`() {
            val paymentId = UUID.randomUUID().toString()
            val pendingPayment = createPayment(status = PaymentStatus.PENDING)
            val changedPayment = pendingPayment.copy(
                status = PaymentStatus.CONFIRMED,
                statusChangedAt = LocalDateTime.now()
            )
            
            every { paymentRepository.findByPaymentId(paymentId) } returns pendingPayment
            every { paymentRepository.upsert(any()) } returns changedPayment
            justRun { confirmOrderUseCase.confirmOrder(pendingPayment.orderNumber) }
            
            val result = paymentService.confirmPayment(paymentId)
            
            assertThat(result).isEqualTo(changedPayment)
            verify(exactly = 1) { confirmOrderUseCase.confirmOrder(pendingPayment.orderNumber) }
        }

        @ParameterizedTest
        @EnumSource(PaymentStatus::class, names = ["PENDING"], mode = EnumSource.Mode.EXCLUDE)
        fun `should throw an error when trying to confirm a non-pending payment`(paymentStatus: PaymentStatus) {
            val paymentId = UUID.randomUUID().toString()
            val payment = createPayment(status = paymentStatus)

            every { paymentRepository.findByPaymentId(paymentId) } returns payment

            assertThatThrownBy { paymentService.confirmPayment(paymentId) }
                .isInstanceOf(PaymentsException::class.java)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_STATE_TRANSITION)
        }
    }

    @Nested
    inner class FailPayment {
        @Test
        fun `should fail payment when state is pending`() {
            val paymentId = UUID.randomUUID().toString()
            val pendingPayment = createPayment(status = PaymentStatus.PENDING)
            val changedPayment = pendingPayment.copy(
                status = PaymentStatus.FAILED,
                statusChangedAt = LocalDateTime.now()
            )

            every { paymentRepository.findByPaymentId(paymentId) } returns pendingPayment
            every { paymentRepository.upsert(any()) } returns changedPayment

            val result = paymentService.failPayment(paymentId)

            assertThat(result).isEqualTo(changedPayment)
        }

        @ParameterizedTest
        @EnumSource(PaymentStatus::class, names = ["PENDING"], mode = EnumSource.Mode.EXCLUDE)
        fun `should throw an error when trying to fail a non-pending payment`(paymentStatus: PaymentStatus) {
            val paymentId = UUID.randomUUID().toString()
            val payment = createPayment(status = paymentStatus)

            every { paymentRepository.findByPaymentId(paymentId) } returns payment

            assertThatThrownBy { paymentService.failPayment(paymentId) }
                .isInstanceOf(PaymentsException::class.java)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_STATE_TRANSITION)
        }
    }

    @Nested
    inner class ExpirePayment {
        @Test
        fun `should expire payment when state is pending`() {
            val paymentId = UUID.randomUUID().toString()
            val pendingPayment = createPayment(status = PaymentStatus.PENDING)
            val changedPayment = pendingPayment.copy(
                status = PaymentStatus.EXPIRED,
                statusChangedAt = LocalDateTime.now()
            )

            every { paymentRepository.findByPaymentId(paymentId) } returns pendingPayment
            every { paymentRepository.upsert(any()) } returns changedPayment

            val result = paymentService.expirePayment(paymentId)

            assertThat(result).isEqualTo(changedPayment)
        }

        @ParameterizedTest
        @EnumSource(PaymentStatus::class, names = ["PENDING"], mode = EnumSource.Mode.EXCLUDE)
        fun `should throw an error when trying to expire a non-pending payment`(paymentStatus: PaymentStatus) {
            val paymentId = UUID.randomUUID().toString()
            val payment = createPayment(status = paymentStatus)

            every { paymentRepository.findByPaymentId(paymentId) } returns payment

            assertThatThrownBy { paymentService.expirePayment(paymentId) }
                .isInstanceOf(PaymentsException::class.java)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_STATE_TRANSITION)
        }
    }
}
