package com.fiap.payments.adapter.gateway

import com.fiap.payments.domain.entities.Payment

interface PaymentGateway {
    fun findByOrderNumber(orderNumber: Long): Payment?

    fun findAll(): List<Payment>

    fun create(payment: Payment): Payment

    fun update(payment: Payment): Payment
}
