package com.fiap.payments.usecases

import com.fiap.payments.domain.entities.Payment

interface ChangePaymentStatusUseCase {
    fun confirmPayment(paymentId: String): Payment
    
    fun failPayment(paymentId: String): Payment
    
    fun expirePayment(paymentId: String): Payment
}
