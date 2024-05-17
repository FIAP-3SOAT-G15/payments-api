package com.fiap.payments.driver.database.persistence.entities

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.fiap.payments.domain.valueobjects.PaymentStatus
import com.fiap.payments.driver.database.configuration.DynamoDBConfig
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@DynamoDBTable(tableName = "payments")
class PaymentDocument(
    @field:DynamoDBHashKey
    @field:DynamoDBAttribute(attributeName = "payment_order_number")
    var orderNumber: String? = null ,

    @field:DynamoDBAttribute(attributeName = "payment_external_order_id")
    var externalOrderId: String = "",

    @field:DynamoDBAttribute(attributeName = "payment_external_order_global_id")
    var externalOrderGlobalId: String? = "",

    @field:DynamoDBAttribute(attributeName = "payment_payment_info")
    var paymentInfo: String = "",

    @field:DynamoDBAttribute(attributeName = "payment_created_at")
    @field:DynamoDBTypeConverted(converter = DynamoDBConfig.Companion.LocalDateTimeConverter::class)
    var createdAt: LocalDateTime? = null,

    @field:DynamoDBAttribute(attributeName = "payment_status")
    var status: String = "",

    @field:DynamoDBAttribute(attributeName = "payment_status_changed_at")
    @field:DynamoDBTypeConverted(converter = DynamoDBConfig.Companion.LocalDateTimeConverter::class)
    var statusChangedAt: LocalDateTime? = null,
)
