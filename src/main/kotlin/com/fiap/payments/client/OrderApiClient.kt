package com.fiap.payments.client


import com.fiap.payments.domain.entities.Order
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(name = "orders-client", url = "\${clients.payments-api.url}")
interface OrderApiClient {

    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/notify/{orderNumber}/confirmed"],
        consumes = ["application/json"]
    )
    fun confirm(@PathVariable orderNumber: Long) : Order

}