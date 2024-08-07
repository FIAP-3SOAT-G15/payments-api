package com.fiap.payments.adapter.messaging

import com.fiap.payments.domain.entities.Payment

interface PaymentSender {
    fun sendPayment(payment: Payment)
}