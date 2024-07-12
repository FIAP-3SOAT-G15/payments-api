package com.fiap.payments.driver.database.config

import com.fiap.payments.PaymentsApiApp
import com.fiap.payments.adapter.gateway.*
import com.fiap.payments.adapter.gateway.impl.*
import com.fiap.payments.adapter.messaging.PaymentSender
import com.fiap.payments.driver.database.persistence.repository.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [PaymentsApiApp::class])
class GatewayConfig {

    @Bean("PaymentGateway")
    fun createPaymentGateway(paymentJpaRepository: PaymentDynamoRepository, paymentSender: PaymentSender): PaymentGateway {
        return PaymentGatewayImpl(paymentJpaRepository, paymentSender)
    }

    @Bean("TransactionalGateway")
    fun createTransactionalGateway(): TransactionalGateway {
        return TransactionalGatewayImpl()
    }

}
