package com.fiap.payments.driver.messaging.consumer

import com.fiap.payments.createPayment
import com.fiap.payments.createPaymentEvent
import com.fiap.payments.usecases.ProvidePaymentRequestUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.messaging.MessageHeaders

class RequestPaymentConsumerTest {

    private val providePaymentRequestUseCase = mockk<ProvidePaymentRequestUseCase>()

    private val consumer = RequestPaymentConsumer(
        providePaymentRequestUseCase
    )

    @Nested
    inner class ConsumeRequest {

        @Test
        fun `should consume payment request`() {
            val event = createPaymentEvent()
            val payment = createPayment()

            every { providePaymentRequestUseCase.providePaymentRequest(event) } returns payment

            consumer.onMessage(event, MessageHeaders(emptyMap()))
        }


    }

}