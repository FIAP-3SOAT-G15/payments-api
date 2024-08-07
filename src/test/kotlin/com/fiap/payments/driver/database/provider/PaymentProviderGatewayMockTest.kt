package com.fiap.payments.driver.database.provider

import com.fiap.payments.createPaymentEvent
import com.fiap.payments.domain.valueobjects.PaymentStatus
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.*

class PaymentProviderGatewayMockTest {
    private val paymentProviderGatewayMock = PaymentProviderGatewayMock()

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
    
    @Test
    fun `should create external order`() {
        val paymentId = UUID.randomUUID().toString()
        val paymentHTTPRequest = createPaymentEvent()
        
        val result = paymentProviderGatewayMock.createExternalOrder(paymentId, paymentHTTPRequest)
 
        assertThat(result.externalOrderId).isNotBlank()
        assertThat(result.paymentInfo).isNotBlank()
    }

    @Test
    fun `should always return confirmed when checking external order status`() {
        val externalOrderGlobalId = UUID.randomUUID().toString()

        val result = paymentProviderGatewayMock.checkExternalOrderStatus(externalOrderGlobalId)

        assertThat(result).isEqualTo(PaymentStatus.CONFIRMED)
    }
}
