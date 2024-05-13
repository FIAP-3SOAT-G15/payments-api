package com.fiap.payments.domain.valueobjects

import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException


enum class ProductCategory {
    DRINK,
    MAIN,
    SIDE,
    DESSERT,
    ;

    companion object {
        fun fromString(category: String): ProductCategory {
            return ProductCategory.values().firstOrNull { it.name.equals(category.trim(), ignoreCase = true) }
                ?: throw PaymentsException(
                    errorType = ErrorType.INVALID_PRODUCT_CATEGORY,
                    message = "Product category $category is not valid",
                )
        }
    }
}
