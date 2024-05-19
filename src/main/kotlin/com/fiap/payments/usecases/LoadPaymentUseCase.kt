package com.fiap.payments.usecases

import com.fiap.payments.domain.entities.Payment

interface LoadPaymentUseCase {
    fun getByPaymentId(id: String): Payment

    fun findAll(): List<Payment>

    fun findByPaymentId(id: String): Payment?
}
