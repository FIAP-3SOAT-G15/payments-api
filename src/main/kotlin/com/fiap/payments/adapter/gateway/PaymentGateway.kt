package com.fiap.payments.adapter.gateway

import com.fiap.payments.domain.entities.Payment

interface PaymentGateway {
    fun findByPaymentId(id: String): Payment?

    fun findAll(): List<Payment>

    fun upsert(payment: Payment): Payment
}
