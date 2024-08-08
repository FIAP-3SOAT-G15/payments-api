package com.fiap.payments

import com.fiap.payments.it.JWTSecurityTestConfig
import com.fiap.payments.it.LocalStackContainerInitializer
import org.junit.jupiter.api.Tag
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@Tag("IntegrationTest")
@ActiveProfiles("test")
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class IntegrationTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
    initializers = [
        LocalStackContainerInitializer::class,
    ],
    classes = [
        JWTSecurityTestConfig::class,
        ServletWebServerFactoryAutoConfiguration::class,
    ]
)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class WithLocalstack