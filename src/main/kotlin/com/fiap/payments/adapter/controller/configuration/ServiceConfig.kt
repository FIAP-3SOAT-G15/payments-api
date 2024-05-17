package com.fiap.payments.adapter.controller.configuration

import com.fiap.payments.PaymentsApp
import com.fiap.payments.adapter.gateway.OrderGateway
import com.fiap.payments.adapter.gateway.PaymentGateway
import com.fiap.payments.adapter.gateway.PaymentProviderGateway
import com.fiap.payments.usecases.ConfirmOrderUseCase
import com.fiap.payments.usecases.LoadPaymentUseCase
import com.fiap.payments.usecases.services.PaymentSyncService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import com.fiap.payments.usecases.services.PaymentService
import com.fiap.payments.usecases.services.OrderService

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
        confirmOrderUseCase: ConfirmOrderUseCase
    ): PaymentSyncService {
        return PaymentSyncService(
            loadPaymentUseCase,
            paymentGateway,
            paymentProvider,
            confirmOrderUseCase
        )
    }

    @Bean
    fun paymentOrderService(orderGateway: OrderGateway): OrderService {
        return OrderService(
            orderGateway
        )
    }

}
