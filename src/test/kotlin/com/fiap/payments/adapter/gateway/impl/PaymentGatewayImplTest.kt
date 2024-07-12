package com.fiap.payments.adapter.gateway.impl

import com.fiap.payments.adapter.messaging.PaymentSender
import com.fiap.payments.createPayment
import com.fiap.payments.createPaymentDocument
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.database.persistence.repository.PaymentDynamoRepository
import com.fiap.payments.toDocument
import com.fiap.payments.toPaymentEntity
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class PaymentGatewayImplTest {
    private val paymentRepository = mockk<PaymentDynamoRepository>()
    private val paymentSender =  mockk<PaymentSender>()


    private val paymentGatewayImpl =
        PaymentGatewayImpl(
            paymentRepository, paymentSender
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should find all payments`() {
        val paymentDocument = createPaymentDocument()
        val paymentDocuments = listOf(paymentDocument)
        val paymentEntities = paymentDocuments.map { it.toPaymentEntity() }

        every { paymentRepository.findAll() } returns paymentDocuments
        
        val result = paymentGatewayImpl.findAll()
        
        assertThat(result).containsExactlyInAnyOrderElementsOf(paymentEntities)
    }

    @Test
    fun `should return existent payment`() {
        val paymentId = UUID.randomUUID().toString()
        val paymentDocument = createPaymentDocument()
        val paymentEntity = paymentDocument.toPaymentEntity()
        
        every { paymentRepository.findById(paymentId) } returns Optional.of(paymentDocument)
        
        val result = paymentGatewayImpl.findByPaymentId(paymentId)
        
        assertThat(result).isEqualTo(paymentEntity)
    }

    @Test
    fun `should return null for non-existent payment`() {
        val paymentId = UUID.randomUUID().toString()

        every { paymentRepository.findById(paymentId) } returns Optional.empty()

        val result = paymentGatewayImpl.findByPaymentId(paymentId)

        assertThat(result).isNull()
    }

    @Nested
    inner class UpsertPayment {

        @Test
        fun `should insert new payment`() {
            val payment = createPayment()
            val paymentDocument = payment.toDocument()

            every { paymentRepository.findById(payment.id) } returns Optional.empty()
            every { paymentRepository.save(any()) } returns paymentDocument

            val result = paymentGatewayImpl.upsert(payment)

            assertThat(result).isEqualTo(payment)
            verify(exactly = 1) { paymentRepository.save(any()) }
        }

        @Test
        fun `should update payment`() {
            val oldPayment = createPayment(id = UUID.randomUUID().toString(), status = PaymentStatus.PENDING)
            val newPayment = oldPayment.copy(status = PaymentStatus.CONFIRMED)
            val oldPaymentDocument = oldPayment.toDocument()
            val newPaymentDocument = newPayment.toDocument()

            every { paymentRepository.findById(newPayment.id) } returns Optional.of(oldPaymentDocument)
            every { paymentRepository.save(any()) } returns newPaymentDocument

            val result = paymentGatewayImpl.upsert(newPayment)

            assertThat(result).isEqualTo(newPayment)
            verify(exactly = 1) { paymentRepository.save(any()) }
        }
    }
}
