package com.fiap.payments.usecases

interface SyncPaymentUseCase {
    fun syncPayment(paymentId: String, externalOrderGlobalId: String)
}
