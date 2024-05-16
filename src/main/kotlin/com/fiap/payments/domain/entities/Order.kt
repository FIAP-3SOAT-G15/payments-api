package com.fiap.payments.domain.entities

import com.fiap.payments.domain.valueobjects.OrderStatus
import java.math.BigDecimal
import java.time.LocalDate

data class Order(
    val number: Long? = null,
    val date: LocalDate = LocalDate.now(),
    val customer: Customer? = null,
    val status: OrderStatus = OrderStatus.CREATED,
    val items: List<Product>,
    val total: BigDecimal,
)
