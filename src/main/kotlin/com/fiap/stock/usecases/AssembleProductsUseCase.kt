package com.fiap.stock.usecases

import com.fiap.stock.domain.entities.Product

interface AssembleProductsUseCase {
    fun compose(
        productNumber: Long,
        subItemsNumbers: List<Long>,
    ): Product?

    fun create(
        product: Product,
        components: List<Long>,
    ): Product

    fun update(
        product: Product,
        components: List<Long>,
    ): Product

    fun delete(productNumber: Long): Product
}
