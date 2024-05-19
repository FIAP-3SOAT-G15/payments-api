package com.fiap.payments.client

import com.fiap.payments.createMercadoPagoMerchantOrderResponse
import com.fiap.payments.createMercadoPagoQRCodeOrderRequest
import com.fiap.payments.createMercadoPagoQRCodeOrderResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class MercadoPagoClientTest {
    private val apiToken = "TOKEN"
    private val apiUrl = "URL"
    private val posId = "POS_ID"
    private val userId = "USER_ID"
    private val restTemplate = mockk<RestTemplate>()

    private val mercadoPagoClient =
        MercadoPagoClient(
            apiToken = apiToken,
            apiUrl = apiUrl,
            posId = posId,
            userId = userId,
            restTemplate = restTemplate,
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Nested
    inner class SubmitMerchantOrder {

        @Test
        fun `should submit merchant order`() {
            val url = "$apiUrl/instore/orders/qr/seller/collectors/$userId/pos/$posId/qrs"
            val request = createMercadoPagoQRCodeOrderRequest()
            val response = ResponseEntity.ok(createMercadoPagoQRCodeOrderResponse())

            every {
                restTemplate.exchange(url, HttpMethod.POST, any(), MercadoPagoQRCodeOrderResponse::class.java)
            } returns response

            val result = mercadoPagoClient.submitMerchantOrder(request)

            assertThat(result).isEqualTo(response.body)
        }

        @Test
        fun `should throw error when provider does not respond to merchant order submission`() {
            val url = "$apiUrl/instore/orders/qr/seller/collectors/$userId/pos/$posId/qrs"
            val request = createMercadoPagoQRCodeOrderRequest()
            val response = ResponseEntity<MercadoPagoQRCodeOrderResponse>(HttpStatus.OK)

            every {
                restTemplate.exchange(url, HttpMethod.POST, any(), MercadoPagoQRCodeOrderResponse::class.java)
            } returns response

            assertThatThrownBy { mercadoPagoClient.submitMerchantOrder(request) }
                .isInstanceOf(IllegalStateException::class.java)
        }
    }

    @Nested
    inner class FetchMerchantOrder {

        @Test
        fun `should fetch merchant order`() {
            val externalOrderGlobalId = "1"
            val url = "$apiUrl/merchant_orders/$externalOrderGlobalId"
            val response = ResponseEntity.ok(createMercadoPagoMerchantOrderResponse())

            every {
                restTemplate.exchange(url, HttpMethod.GET, any(), MercadoPagoMerchantOrderResponse::class.java)
            } returns response

            val result = mercadoPagoClient.fetchMerchantOrder(externalOrderGlobalId)

            assertThat(result).isEqualTo(response.body)
        }

        @Test
        fun `should throw error when provider does not respond to merchant order fetching`() {
            val externalOrderGlobalId = "1"
            val url = "$apiUrl/merchant_orders/$externalOrderGlobalId"
            val response = ResponseEntity<MercadoPagoQRCodeOrderResponse>(HttpStatus.OK)

            every {
                restTemplate.exchange(url, HttpMethod.POST, any(), MercadoPagoQRCodeOrderResponse::class.java)
            } returns response

            assertThatThrownBy { mercadoPagoClient.fetchMerchantOrder(externalOrderGlobalId) }
                .isInstanceOf(IllegalStateException::class.java)
        }
    }

    @Test
    fun `should throw error when communicating to the provider`() {
        val externalOrderGlobalId = "1"
        val url = "$apiUrl/merchant_orders/$externalOrderGlobalId"

        every {
            restTemplate.exchange(url, HttpMethod.POST, any(), MercadoPagoQRCodeOrderResponse::class.java)
        } throws RuntimeException()

        assertThatThrownBy { mercadoPagoClient.fetchMerchantOrder(externalOrderGlobalId) }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
