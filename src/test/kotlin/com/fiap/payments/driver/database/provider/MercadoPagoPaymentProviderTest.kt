package com.fiap.payments.driver.database.provider

import com.fiap.payments.client.MercadoPagoClient
import com.fiap.payments.createMercadoPagoMerchantOrderResponse
import com.fiap.payments.createMercadoPagoQRCodeOrderResponse
import com.fiap.payments.createPaymentEvent
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.database.provider.MercadoPagoPaymentProvider.MercadoPagoOrderStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

class MercadoPagoPaymentProviderTest {
    private val mercadoPagoClient = mockk<MercadoPagoClient>()
    private val webhookBaseUrl = "URL"

    private val mercadoPagoPaymentProvider = MercadoPagoPaymentProvider(
        mercadoPagoClient = mercadoPagoClient,
        webhookBaseUrl = webhookBaseUrl
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
    
    @Test
    fun `should create external order`() {
        val paymentId = UUID.randomUUID().toString()
        val paymentHTTPRequest = createPaymentEvent()
        val mercadoPagoQRCodeOrderResponse = createMercadoPagoQRCodeOrderResponse()
        
        every { mercadoPagoClient.submitMerchantOrder(any()) } returns mercadoPagoQRCodeOrderResponse

        val result = mercadoPagoPaymentProvider.createExternalOrder(paymentId, paymentHTTPRequest)

        assertThat(result.externalOrderId).isEqualTo(mercadoPagoQRCodeOrderResponse.inStoreOrderId)
        assertThat(result.paymentInfo).isEqualTo(mercadoPagoQRCodeOrderResponse.qrData)
    }

    @ParameterizedTest
    @MethodSource("provideMercadoPagoOrderStatusToPaymentStatus")
    fun `should always return confirmed when checking external order status`(
        orderStatus: String,
        paymentStatus: PaymentStatus
    ) {
        val externalOrderGlobalId = UUID.randomUUID().toString()
        val mercadoPagoMerchantOrderResponse = createMercadoPagoMerchantOrderResponse(
            orderStatus = orderStatus
        )

        every { mercadoPagoClient.fetchMerchantOrder(externalOrderGlobalId) } returns mercadoPagoMerchantOrderResponse

        val result = mercadoPagoPaymentProvider.checkExternalOrderStatus(externalOrderGlobalId)

        assertThat(result).isEqualTo(paymentStatus)
    }

    companion object {
        @JvmStatic
        private fun provideMercadoPagoOrderStatusToPaymentStatus() = Stream.of(
            Arguments.of(MercadoPagoOrderStatus.PAID.orderStatus, PaymentStatus.CONFIRMED),
            Arguments.of(MercadoPagoOrderStatus.EXPIRED.orderStatus, PaymentStatus.EXPIRED),
            Arguments.of(MercadoPagoOrderStatus.PAYMENT_IN_PROCESS.orderStatus, PaymentStatus.PENDING),
            Arguments.of(MercadoPagoOrderStatus.PAYMENT_REQUIRED.orderStatus, PaymentStatus.PENDING),
            Arguments.of("other", PaymentStatus.FAILED),
        )
    }
}
