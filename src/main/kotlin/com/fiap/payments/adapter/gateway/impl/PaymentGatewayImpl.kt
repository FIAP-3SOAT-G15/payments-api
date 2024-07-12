package com.fiap.payments.adapter.gateway.impl

import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.messaging.PaymentSender
import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.driver.database.persistence.mapper.PaymentMapper
import com.fiap.payments.driver.database.persistence.repository.PaymentDynamoRepository
import org.mapstruct.factory.Mappers

class PaymentGatewayImpl(
    private val paymentRepository: PaymentDynamoRepository,
    private val paymentSender: PaymentSender
) : PaymentGateway {
    private val mapper = Mappers.getMapper(PaymentMapper::class.java)

    override fun findByPaymentId(id: String): Payment? {
        return paymentRepository.findById(id)
            .map(mapper::toDomain)
            .orElse(null)
    }

    override fun findAll(): List<Payment> {
        return paymentRepository.findAll()
            .map(mapper::toDomain)
    }

    override fun upsert(payment: Payment): Payment {
        val currentPayment = findByPaymentId(id = payment.id) ?: payment
        val paymentUpdated =
            currentPayment.copy(
                orderNumber = payment.orderNumber,
                externalOrderId = payment.externalOrderId,
                externalOrderGlobalId = payment.externalOrderGlobalId,
                paymentInfo = payment.paymentInfo,
                createdAt = payment.createdAt,
                status = payment.status,
                statusChangedAt = payment.statusChangedAt,
            )
        return persist(paymentUpdated)
    }

    override fun publishPayment(payment: Payment): Payment {
        paymentSender.sendPayment(payment)
        return payment
    }

    private fun persist(payment: Payment): Payment =
        payment
            .let(mapper::toEntity)
            .let(paymentRepository::save)
            .let(mapper::toDomain)
}
