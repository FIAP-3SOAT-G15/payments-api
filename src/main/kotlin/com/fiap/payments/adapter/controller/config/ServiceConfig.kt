package com.fiap.payments.adapter.controller.config

import com.fiap.payments.PaymentsApiApp
import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.usecases.ChangePaymentStatusUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.services.PaymentService
import com.fiap.payments.usecases.services.PaymentSyncService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [PaymentsApiApp::class])
class ServiceConfig {

    @Bean
    fun createPaymentService(
        paymentRepository: PaymentGateway,
        paymentProvider: PaymentProviderGateway
    ): PaymentService {
        return PaymentService(
            paymentRepository,
            paymentProvider,
        )
    }

    @Bean
    fun paymentSyncService(
        loadPaymentUseCase: LoadPaymentUseCase,
        paymentGateway: PaymentGateway,
        paymentProvider: PaymentProviderGateway,
        changePaymentStatusUseCase: ChangePaymentStatusUseCase,
    ): PaymentSyncService {
        return PaymentSyncService(
            loadPaymentUseCase,
            paymentGateway,
            paymentProvider,
            changePaymentStatusUseCase
        )
    }

}
