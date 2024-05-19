package com.fiap.payments.application.services

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException
import com.fiap.payments.usecases.ConfirmOrderUseCase
import com.fiap.payments.usecases.services.PaymentService
import createPayment
import createPaymentHTTPRequest
import createPaymentRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
    inner class GetByOrderNumberTest {
        @Test
        fun `should return a payment when it exists`() {
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
    inner class ProvidePaymentRequestTest {
        @Test
        fun `should create payment`() {
            val paymentHTTPRequest = createPaymentHTTPRequest()
            val payment = createPayment()
            val paymentRequest = createPaymentRequest()

            every { paymentRepository.upsert(any()) } returns payment
            every { paymentProvider.createExternalOrder(payment.id, paymentHTTPRequest) } returns paymentRequest

            val result = paymentService.providePaymentRequest(paymentHTTPRequest)

            assertThat(result).isNotNull()
            assertThat(result.externalOrderId).isEqualTo(paymentRequest.externalOrderId)
            assertThat(result.paymentInfo).isEqualTo(paymentRequest.paymentInfo)

            verify { paymentRepository.upsert(any()) }
        }
    }
}
