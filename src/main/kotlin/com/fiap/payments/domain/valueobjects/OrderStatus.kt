package com.fiap.payments.domain.valueobjects

import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.paymentsException

enum class OrderStatus {
    CREATED,
    PENDING,
    CONFIRMED,
    PREPARING,
    COMPLETED,
    DONE,
    CANCELLED,
    ;

    companion object {
        fun fromString(status: String): OrderStatus {
            return values().firstOrNull { it.name.equals(status.trim(), ignoreCase = true) }
                ?: throw paymentsException(
                    errorType = ErrorType.INVALID_ORDER_STATUS,
                    message = "Status $status is not valid",
                )
        }
    }
}
