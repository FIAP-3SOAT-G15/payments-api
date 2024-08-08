package com.fiap.payments.driver.messaging.consumer

import com.fiap.payments.driver.messaging.event.PaymentRequestEvent
import com.fiap.payments.usecases.ProvidePaymentRequestUseCase
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component

@Component
@EnableAsync
class RequestPaymentConsumer(
    private val providePaymentRequestUseCase: ProvidePaymentRequestUseCase) {

    private val log = LoggerFactory.getLogger(javaClass)


    @SqsListener("\${sqs.queues.request-payment}")
    fun onMessage(message: PaymentRequestEvent, @Headers headers: MessageHeaders) {
        log.info(message.toString())
        providePaymentRequestUseCase.providePaymentRequest(message)
    }
}


