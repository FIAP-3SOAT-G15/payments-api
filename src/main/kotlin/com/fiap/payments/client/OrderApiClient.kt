package com.fiap.payments.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(
    name = "orders-client",
    url = "\${clients.orders-api.url}"
)
interface OrderApiClient {

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/orders/{orderNumber}/confirm"],
        consumes = ["application/json"]
    )
    fun confirm(@PathVariable orderNumber: Long)
}
