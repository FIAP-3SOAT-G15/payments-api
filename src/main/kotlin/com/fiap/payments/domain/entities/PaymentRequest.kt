package com.fiap.payments.domain.entities

class PaymentRequest(
    val externalOrderId: String,
    val externalOrderGlobalId: String?,
    val paymentInfo: String,
)
