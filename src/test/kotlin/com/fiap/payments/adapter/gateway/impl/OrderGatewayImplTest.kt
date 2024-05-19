package com.fiap.payments.adapter.gateway.impl

import com.fiap.payments.client.OrderApiClient
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class OrderGatewayImplTest {
    private val orderApiClient = mockk<OrderApiClient>()

    private val orderGatewayImpl =
        OrderGatewayImpl(
            orderApiClient
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
    
    @Test
    fun `should confirm order`() {
        val orderNumber = 1L

        justRun { orderApiClient.confirm(orderNumber) }

        orderGatewayImpl.confirmOrder(orderNumber)

        verify { orderApiClient.confirm(orderNumber) }
    }
}
