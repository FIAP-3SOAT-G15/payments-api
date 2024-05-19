package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.createPayment
import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.usecases.ChangePaymentStatusUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

class PaymentSyncServiceTest {
    private val loadPaymentUseCase = mockk<LoadPaymentUseCase>()
    private val paymentGateway = mockk<PaymentGateway>()
    private val paymentProviderGateway = mockk<PaymentProviderGateway>()
    private val changePaymentStatusUseCase = mockk<ChangePaymentStatusUseCase>()

    private val paymentSyncService =
        PaymentSyncService(
            loadPaymentUseCase,
            paymentGateway,
            paymentProviderGateway,
            changePaymentStatusUseCase,
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should should not update internal payment status when external status did not change`() {
        val paymentId = UUID.randomUUID().toString()
        val externalOrderGlobalId = "externalOrderGlobalId"
        val payment = createPayment(status = PaymentStatus.PENDING, externalOrderGlobalId = null)
        val newStatus = PaymentStatus.PENDING

        every { loadPaymentUseCase.getByPaymentId(paymentId) } returns payment
        every { paymentGateway.upsert(any()) } returns mockk()
        every { paymentProviderGateway.checkExternalOrderStatus(externalOrderGlobalId) } returns newStatus

        paymentSyncService.syncPayment(paymentId, externalOrderGlobalId)
        
        verify(exactly = 0) {
            changePaymentStatusUseCase.failPayment(any())
            changePaymentStatusUseCase.expirePayment(any())
            changePaymentStatusUseCase.confirmPayment(any())
        }
    }

    @ParameterizedTest
    @MethodSource("provideNewPaymentStatus")
    fun `should update payment internal payment status when external status did change`(
        newStatus: PaymentStatus,
        failCount: Int,
        expireCount: Int,
        confirmCount: Int,
    ) {
        val paymentId = UUID.randomUUID().toString()
        val externalOrderGlobalId = "externalOrderGlobalId"
        val payment = createPayment(status = PaymentStatus.PENDING, externalOrderGlobalId = null)

        every { loadPaymentUseCase.getByPaymentId(paymentId) } returns payment
        every { paymentGateway.upsert(any()) } returns mockk()
        every { paymentProviderGateway.checkExternalOrderStatus(externalOrderGlobalId) } returns newStatus
        every {
            changePaymentStatusUseCase.failPayment(paymentId)
            changePaymentStatusUseCase.expirePayment(paymentId)
            changePaymentStatusUseCase.confirmPayment(paymentId)
        } returns payment.copy(status = newStatus)

        paymentSyncService.syncPayment(paymentId, externalOrderGlobalId)

        verify(exactly = failCount) { changePaymentStatusUseCase.failPayment(any()) }
        verify(exactly = expireCount) { changePaymentStatusUseCase.expirePayment(any()) }
        verify(exactly = confirmCount) { changePaymentStatusUseCase.confirmPayment(any()) }
    }

    companion object {
        @JvmStatic
        private fun provideNewPaymentStatus() = Stream.of(
            Arguments.of(PaymentStatus.FAILED, 1, 0, 0),
            Arguments.of(PaymentStatus.EXPIRED, 0, 1, 0),
            Arguments.of(PaymentStatus.CONFIRMED, 0, 0, 1),
        )
    }
    
    @Test
    fun `should throw an error when trying to change payment status to PENDING`() {
        val paymentId = UUID.randomUUID().toString()
        val externalOrderGlobalId = "externalOrderGlobalId"
        val payment = createPayment(status = PaymentStatus.EXPIRED, externalOrderGlobalId = null)
        val newStatus = PaymentStatus.PENDING

        every { loadPaymentUseCase.getByPaymentId(paymentId) } returns payment
        every { paymentGateway.upsert(any()) } returns mockk()
        every { paymentProviderGateway.checkExternalOrderStatus(externalOrderGlobalId) } returns newStatus
        
        assertThatThrownBy { paymentSyncService.syncPayment(paymentId, externalOrderGlobalId) }
            .isInstanceOf(PaymentsException::class.java)
            .hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_PAYMENT_STATE_TRANSITION)
    }
}
