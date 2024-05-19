package com.fiap.payments.driver.web.request

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentHTTPRequest(
    val orderInfo: OrderInfo,
)

data class OrderInfo(
    @Schema(title = "Número de pedido", example = "1", required = true)
    val number: Long,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "Valor total", example = "10.00", required = true)
    val totalAmount: BigDecimal,
    @ArraySchema(
        schema = Schema(implementation = OrderLine::class, required = true),
        minItems = 1
    )
    val lines: List<OrderLine>,
    @Schema(title = "Data do pedido", example = "2024-05-19T11:00:00", required = true)
    val orderedAt: LocalDateTime,
    @Schema(title = "Solicitante", example = "John Doe", required = true)
    val orderedBy: String,
)

data class OrderLine(
    @Schema(title = "Nome do item de pedido", example = "Big Mac", required = true)
    val name: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "Valor unitário", example = "10.00", required = true)
    val unitPrice: BigDecimal,
    @Schema(title = "Quantidade", example = "1", required = true)
    val quantity: Long,
    @Schema(title = "Unidade de medida", example = "UND", required = true)
    val unitOfMeasurement: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "Valor total", example = "10.00", required = true)
    val totalAmount: BigDecimal,
)
