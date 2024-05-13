package com.fiap.payments.adapter.gateway.impl

import com.fiap.payments.adapter.gateway.TransactionalGateway
import org.springframework.transaction.annotation.Transactional

open class TransactionalGatewayImpl : TransactionalGateway {
    @Transactional
    override fun <T> transaction(code: () -> T): T {
        return code()
    }
}
