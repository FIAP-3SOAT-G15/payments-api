package com.fiap.payments.adapter.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fiap.payments.adapter.messaging.impl.PaymentSenderImpl
import com.fiap.payments.createPayment
import io.awspring.cloud.sns.core.SnsTemplate
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.springframework.messaging.support.GenericMessage

class PaymentSenderImplTest {

    private val snsTemplate: SnsTemplate = mockk<SnsTemplate>()
    private val topicName: String = "topic"
    private val mapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    private val sender = PaymentSenderImpl(
        snsTemplate = snsTemplate,
        topicName = topicName,
        mapper = mapper
    )

    @Nested
    inner class SendPayment {

        @Test
        fun `should send payment`() {
            val payment = createPayment()

            justRun { snsTemplate.send(eq(topicName), any(GenericMessage::class)) }

            sender.sendPayment(payment)
        }

    }


}