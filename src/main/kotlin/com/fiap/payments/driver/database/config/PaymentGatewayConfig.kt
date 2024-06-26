package com.fiap.payments.driver.database.config

import com.fiap.payments.PaymentsApiApp
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.client.MercadoPagoClient
import com.fiap.payments.driver.database.provider.MercadoPagoPaymentProvider
import com.fiap.payments.driver.database.provider.PaymentProviderGatewayMock
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [PaymentsApiApp::class])
class PaymentGatewayConfig {

    @Bean("PaymentProvider")
    @ConditionalOnProperty("payment-provider.mock", havingValue = "false")
    fun createPaymentProvider(
        mercadoPagoClient: MercadoPagoClient,
        @Value("\${mercadopago.integration.webhookBaseUrl}") webhookBaseUrl: String,
    ): PaymentProviderGateway {
        return MercadoPagoPaymentProvider(mercadoPagoClient, webhookBaseUrl)
    }
    
    @Bean("PaymentProvider")
    @ConditionalOnProperty("payment-provider.mock", havingValue = "true")
    fun createPaymentProviderMock(): PaymentProviderGateway {
        return PaymentProviderGatewayMock()
    }
}
