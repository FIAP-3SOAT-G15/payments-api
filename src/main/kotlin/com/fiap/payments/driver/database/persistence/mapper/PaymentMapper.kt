package com.fiap.payments.driver.database.persistence.mapper

import com.fiap.payments.domain.entities.Payment
import com.fiap.payments.driver.database.persistence.entities.PaymentDocument
import org.mapstruct.Mapper

@Mapper
interface PaymentMapper {
    fun toDomain(entity: PaymentDocument): Payment

    fun toEntity(domain: Payment): PaymentDocument
}
