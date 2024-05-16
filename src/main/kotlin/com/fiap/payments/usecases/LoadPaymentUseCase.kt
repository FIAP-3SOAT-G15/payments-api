package com.fiap.payments.usecases

import com.fiap.payments.domain.entities.Payment

interface LoadPaymentUseCase {
    fun getByOrderNumber(orderNumber: Long): Payment

    fun findAll(): List<Payment>

    fun findByOrderNumber(orderNumber: Long): Payment?
}
