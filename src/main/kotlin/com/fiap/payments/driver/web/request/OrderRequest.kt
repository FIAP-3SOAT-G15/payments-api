package com.fiap.payments.driver.web.request

import com.fiap.payments.domain.entities.Customer
import com.fiap.payments.domain.entities.Order
import com.fiap.payments.domain.entities.Product
import com.fiap.payments.domain.valueobjects.OrderStatus
import java.math.BigDecimal
import java.time.LocalDate

data class OrderRequest(
    val number: Long? = null,
    val date: LocalDate = LocalDate.now(),
    val customer: Customer? = null,
    val status: OrderStatus,
    val items: List<ProductRequest>,
    val total: BigDecimal,
) {
    fun toDomain(): Order {
        return Order(
            number = number,
            date = date,
            customer = customer,
            status = status,
            total = total,
            items = items.map {
                Product(
                    name = it.name,
                    number = it.number,
                    price =  it.price,
                    description = it.description,
                    category = it.category,
                    minSub = 0,
                    maxSub = Int.MAX_VALUE,
                    subItems = arrayListOf(),
                    components = arrayListOf()
                )
            }
        )
    }
}
