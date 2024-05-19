package com.fiap.payments

import org.junit.jupiter.api.Tag
import org.springframework.test.context.ActiveProfiles

@Tag("IntegrationTest")
@ActiveProfiles("test")
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class IntegrationTest
