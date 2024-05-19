package com.fiap.payments

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@OpenAPIDefinition(
    info =
        Info(
            title = "Payments API",
            version = "1.0.0",
            description = "Microsserviço de pagamentos",
            contact =
                Contact(
                    name = "Grupo 15",
                    url = "http://fiap-3soat-g15.s3-website-us-east-1.amazonaws.com",
                ),
        ),
    servers = [
        Server(url = "/"),
    ],
)
@EnableFeignClients
@ImportAutoConfiguration(value = [FeignAutoConfiguration::class])
class PaymentsApiApp

fun main(args: Array<String>) {
    runApplication<PaymentsApiApp>(*args)
}
