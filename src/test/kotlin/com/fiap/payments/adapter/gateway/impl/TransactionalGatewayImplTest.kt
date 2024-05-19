package com.fiap.payments.adapter.gateway.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransactionalGatewayImplTest {
    private val transactionalGatewayImpl = TransactionalGatewayImpl()

    @Test
    fun `test transaction executes code block`() {
        val expectedResult = "Success"
        val codeBlock: () -> String = mockk()

        every { codeBlock.invoke() } returns expectedResult

        val result = transactionalGatewayImpl.transaction(codeBlock)

        assertEquals(expectedResult, result)
        verify(exactly = 1) { codeBlock.invoke() }
    }
}
