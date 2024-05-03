package com.fiap.stock.adapter.gateway.impl

import com.fiap.stock.adapter.gateway.ProductGateway
import com.fiap.stock.domain.entities.Product
import com.fiap.stock.domain.errors.ErrorType
import com.fiap.stock.domain.errors.SelfOrderManagementException
import com.fiap.stock.domain.valueobjects.ProductCategory
import com.fiap.stock.driver.database.persistence.jpa.ProductJpaRepository
import com.fiap.stock.driver.database.persistence.mapper.ProductMapper
import org.mapstruct.factory.Mappers

class ProductGatewayImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductGateway {
    private val mapper: ProductMapper = Mappers.getMapper(ProductMapper::class.java)

    override fun findAll(): List<Product> {
        return productJpaRepository.findAll()
            .map(mapper::toDomain)
    }

    override fun findByProductNumber(productNumber: Long): Product? {
        return productJpaRepository.findById(productNumber)
            .map { mapper.toDomain(it) }
            .orElse(null)
    }

    override fun findByCategory(category: ProductCategory): List<Product> {
        return productJpaRepository.findByCategoryIgnoreCase(category.toString())
            .map { mapper.toDomain(it) }
    }

    override fun searchByName(name: String): List<Product> {
        return productJpaRepository.findByNameContainingIgnoreCase(name)
            .map(mapper::toDomain)
    }

    override fun create(product: Product): Product {
        return persist(product.copy(number = null))
    }

    override fun update(product: Product): Product {
        val number =
            product.number ?: throw SelfOrderManagementException(
                errorType = ErrorType.PRODUCT_NUMBER_IS_MANDATORY,
                message = "Product ${product.name} not identified by number",
            )
        val newItem =
            findByProductNumber(number)?.update(product)
                ?: throw SelfOrderManagementException(
                    errorType = ErrorType.PRODUCT_NOT_FOUND,
                    message = "Product [${product.number}] not found",
                )
        return persist(newItem)
    }

    override fun delete(productNumber: Long): Product {
        return findByProductNumber(productNumber)?.let {
            productJpaRepository.deleteById(productNumber)
            it
        }
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.PRODUCT_NOT_FOUND,
                message = "Product [$productNumber] not found",
            )
    }

    override fun deleteAll() {
        productJpaRepository.deleteAll()
    }

    private fun persist(product: Product): Product =
        product
            .let(mapper::toEntity)
            .let(productJpaRepository::save)
            .let(mapper::toDomain)
}
