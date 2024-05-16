package com.fiap.payments.driver.database.configuration

import com.fiap.payments.PaymentsApp
import com.fiap.payments.adapter.gateway.*
import com.fiap.payments.adapter.gateway.impl.*
import com.fiap.payments.driver.database.persistence.repository.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [PaymentsApp::class])
class GatewayConfig {

    @Bean("PaymentGateway")
    fun createPaymentGateway(paymentJpaRepository: PaymentDynamoRepository): PaymentGateway {
        return PaymentGatewayImpl(paymentJpaRepository)
    }

    @Bean("TransactionalGateway")
    fun createTransactionalGateway(): TransactionalGateway {
        return TransactionalGatewayImpl()
    }
}
