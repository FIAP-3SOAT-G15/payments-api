package com.fiap.payments.driver.web.request

import com.fiap.payments.domain.valueobjects.ProductCategory
import java.math.BigDecimal

data class ProductRequest(
    val number: Long? = null,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val category: ProductCategory,
)
