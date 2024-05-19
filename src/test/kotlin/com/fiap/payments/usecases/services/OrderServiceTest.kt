package com.fiap.payments.usecases.services

import com.fiap.payments.adapter.gateway.OrderGateway
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class OrderServiceTest {
    private val orderGateway = mockk<OrderGateway>()

    private val orderService = OrderService(
        orderGateway,
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should confirm order`() {
        val orderNumber = 1L
        justRun { orderGateway.confirmOrder(orderNumber) }
        orderService.confirmOrder(orderNumber)
        verify(exactly = 1) { orderGateway.confirmOrder(orderNumber) }
    }
}
