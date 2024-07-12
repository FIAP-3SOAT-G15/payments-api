package com.fiap.payments.adapter.messaging.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fiap.payments.adapter.messaging.PaymentSender
import com.fiap.payments.domain.entities.Payment
import io.awspring.cloud.sns.core.SnsTemplate
import org.springframework.messaging.support.GenericMessage

class PaymentSenderImpl(
    private val snsTemplate: SnsTemplate,
    private val topicName: String,
    private val mapper: ObjectMapper
) : PaymentSender {

    override fun sendPayment(payment: Payment) {
        snsTemplate.send(
            topicName, GenericMessage(mapper.writeValueAsString(payment))
        )
    }
}