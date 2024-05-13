package com.fiap.payments.usecases

import com.fiap.payments.domain.entities.Order
import com.fiap.payments.domain.entities.PaymentRequest

interface ProvidePaymentRequestUseCase {
    fun providePaymentRequest(order: Order): PaymentRequest
}
