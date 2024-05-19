package com.fiap.payments.usecases

import com.fiap.payments.domain.entities.Payment

interface LoadPaymentUseCase {
    fun getByPaymentId(paymentId: String): Payment

    fun findAll(): List<Payment>

    fun findByPaymentId(paymentId: String): Payment?
}
