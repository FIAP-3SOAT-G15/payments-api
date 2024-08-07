package com.fiap.payments.driver.web

import com.fiap.payments.domain.entities.Payment
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "pagamento", description = "Pagamentos")
@RequestMapping("/payments")
interface PaymentAPI {
    @Operation(summary = "Retorna todos os pagamentos")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
        ],
    )
    @GetMapping
    fun findAll(): ResponseEntity<List<Payment>>

    @Operation(summary = "Retorna pagamento do pedido")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        ],
    )
    @GetMapping("/{paymentId}")
    fun getByPaymentId(
        @Parameter(description = "ID de pagamento") @PathVariable paymentId: String,
    ): ResponseEntity<Payment>

    @Operation(
        summary = "Notificações de pagamento",
        parameters = [
            Parameter(
                name = "x-signature",
                required = true,
                `in` = ParameterIn.HEADER,
                schema = Schema(
                    type = "string",
                    defaultValue = "ts=1704908010,v1=618c85345248dd820d5fd456117c2ab2ef8eda45a0282ff693eac24131a5e839"
                ),
            ),
        ],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
        ],
    )
    @PostMapping("/notifications/{paymentId}")
    fun notify(
        @Parameter(description = "ID de pagamento") @PathVariable paymentId: String,
        @RequestParam(value = "id") resourceId: String,
        @RequestParam topic: String,
    ): ResponseEntity<Any>


    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
        ],
    )
    @PostMapping("/{paymentId}/fail")
    fun fail(
        @Parameter(description = "ID de pagamento") @PathVariable paymentId: String,
    ): ResponseEntity<Payment>

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
        ],
    )
    @PostMapping("/{paymentId}/expire")
    fun expire(
        @Parameter(description = "ID de pagamento") @PathVariable paymentId: String,
    ): ResponseEntity<Payment>

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
        ],
    )
    @PostMapping("/{paymentId}/confirm")
    fun confirm(
        @Parameter(description = "ID de pagamento") @PathVariable paymentId: String,
    ): ResponseEntity<Payment>
}
