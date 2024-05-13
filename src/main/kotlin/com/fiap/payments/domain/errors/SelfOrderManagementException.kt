package com.fiap.payments.domain.errors

data class paymentsException(var errorType: ErrorType, override val cause: Throwable? = null, override val message: String?) :
    RuntimeException(message, cause)
