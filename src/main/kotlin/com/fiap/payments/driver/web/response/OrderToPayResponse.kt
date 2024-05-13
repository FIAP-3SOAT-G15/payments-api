package com.fiap.payments.driver.web.response

import com.fiap.payments.domain.entities.Order

data class OrderToPayResponse(
    val order: Order,
    val paymentInfo: String,
)
