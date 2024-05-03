package com.fiap.stock.driver.web.request

import com.fiap.stock.domain.entities.Component
import io.swagger.v3.oas.annotations.media.Schema

data class ComponentRequest(
    @Schema(title = "Nome do componente", example = "Hambúrguer", required = true)
    val name: String,
    @Schema(title = "Quantidade inicial", example = "100", required = true)
    val initialQuantity: Long,
) {
    fun toComponent(): Component {
        return Component(name = name)
    }
}
