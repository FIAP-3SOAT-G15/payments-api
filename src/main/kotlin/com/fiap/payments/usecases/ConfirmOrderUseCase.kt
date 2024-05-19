package com.fiap.payments.usecases

interface ConfirmOrderUseCase {
    fun confirmOrder(orderNumber: Long)
}
