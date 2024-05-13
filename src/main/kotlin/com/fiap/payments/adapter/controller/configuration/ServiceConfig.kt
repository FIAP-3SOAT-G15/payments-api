package com.fiap.payments.adapter.controller.configuration

import com.fiap.payments.PaymentsApp
import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.services.PaymentSyncService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import services.PaymentService

@Configuration
@ComponentScan(basePackageClasses = [PaymentsApp::class])
class ServiceConfig {


    @Bean
    fun createPaymentService(
        paymentRepository: PaymentGateway,
        paymentProvider: PaymentProviderGateway,
    ): PaymentService {
        return PaymentService(
            paymentRepository,
            paymentProvider
        )
    }
    
    @Bean
    fun paymentSyncService(
        loadPaymentUseCase: LoadPaymentUseCase,
        paymentGateway: PaymentGateway,
        paymentProvider: PaymentProviderGateway,
    ): PaymentSyncService {
        return PaymentSyncService(
            loadPaymentUseCase,
            paymentGateway,
            paymentProvider,
        )
    }
}
