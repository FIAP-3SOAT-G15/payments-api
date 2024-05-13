package com.fiap.payments.usecases

interface SyncPaymentUseCase {

    fun syncPayment(orderNumber: Long, externalOrderGlobalId: String)
}
