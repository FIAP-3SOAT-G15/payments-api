package com.fiap.payments.usecases

import com.fiap.payments.domain.entities.Order

interface ConfirmOrderUseCase {
    fun confirmOrder(orderNumber: Long): Order
}
