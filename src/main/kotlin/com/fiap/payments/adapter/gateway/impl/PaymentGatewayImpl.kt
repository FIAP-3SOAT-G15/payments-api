package com.fiap.payments.adapter.gateway.impl

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.paymentsException
import com.fiap.payments.driver.database.persistence.repository.PaymentDynamoRepository
import com.fiap.payments.driver.database.persistence.mapper.PaymentMapper
import org.mapstruct.factory.Mappers

class PaymentGatewayImpl(
    private val paymentJpaRepository: PaymentDynamoRepository,
) : PaymentGateway {
    private val mapper = Mappers.getMapper(PaymentMapper::class.java)

    override fun findByOrderNumber(orderNumber: Long): Payment? {
        return paymentJpaRepository.findById(orderNumber)
            .map(mapper::toDomain)
            .orElse(null)
    }

    override fun findAll(): List<Payment> {
        return paymentJpaRepository.findAll()
            .map(mapper::toDomain)
    }

    override fun create(payment: Payment): Payment {
        payment.orderNumber.let {
            findByOrderNumber(it)?.let {
                throw paymentsException(
                    errorType = ErrorType.PAYMENT_ALREADY_EXISTS,
                    message = "Payment record for order [${payment.orderNumber}] already exists",
                )
            }
        }
        return persist(payment)
    }

    override fun update(payment: Payment): Payment {
        val newItem =
            payment.orderNumber.let { findByOrderNumber(it)?.update(payment) }
                ?: throw paymentsException(
                    errorType = ErrorType.PAYMENT_NOT_FOUND,
                    message = "Payment record for order [${payment.orderNumber}] not found",
                )
        return persist(newItem)
    }

    private fun persist(payment: Payment): Payment =
        payment
            .let(mapper::toEntity)
            .let(paymentJpaRepository::save)
            .let(mapper::toDomain)
}
