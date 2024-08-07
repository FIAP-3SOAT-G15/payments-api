package com.fiap.payments.adapter.messaging.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fiap.payments.adapter.messaging.PaymentSender
import com.fiap.payments.adapter.messaging.impl.PaymentSenderImpl
import io.awspring.cloud.sns.core.SnsTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingConfig {

    @Bean
    fun createPaymentSender(snsTemplate: SnsTemplate,
                            @Value("\${topic.response-payment}") topicName: String,
                            objectMapper: ObjectMapper): PaymentSender {
        return PaymentSenderImpl(snsTemplate, topicName, objectMapper)
    }
}