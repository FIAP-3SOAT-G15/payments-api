package com.fiap.payments.domain.entities

import com.fiap.payments.domain.valueobjects.PaymentStatus
import java.time.LocalDateTime
import java.util.*

data class Payment(
    val id: String = UUID.randomUUID().toString(),
    val orderNumber: Long,
    val externalOrderId: String? = null,
    val externalOrderGlobalId: String? = null,
    val paymentInfo: String? = null,
    val createdAt: LocalDateTime,
    val status: PaymentStatus,
    val statusChangedAt: LocalDateTime,
)
