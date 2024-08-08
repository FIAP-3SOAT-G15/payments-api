package com.fiap.payments.it

import com.fiap.payments.IntegrationTest
import com.fiap.payments.WithLocalstack
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@IntegrationTest
@WithLocalstack
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicIntegrationTest {

    @Test
    fun contextLoads() { }
}