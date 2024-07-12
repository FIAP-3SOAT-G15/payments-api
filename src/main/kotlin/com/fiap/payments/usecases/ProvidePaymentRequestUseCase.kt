package com.fiap.payments.usecases

import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.driver.messaging.event.PaymentRequestEvent
import com.fiap.payments.driver.web.request.PaymentHTTPRequest

interface ProvidePaymentRequestUseCase {
    fun providePaymentRequest(paymentRequestEvent: PaymentRequestEvent): Payment
}
