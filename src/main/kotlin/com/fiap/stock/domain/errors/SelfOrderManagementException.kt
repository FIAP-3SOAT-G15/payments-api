package com.fiap.stock.domain.errors

data class SelfOrderManagementException(var errorType: ErrorType, override val cause: Throwable? = null, override val message: String?) :
    RuntimeException(message, cause)
