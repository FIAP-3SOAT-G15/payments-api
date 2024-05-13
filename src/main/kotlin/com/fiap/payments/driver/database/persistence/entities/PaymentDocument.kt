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
    val orderNumber: Long,

    @field:DynamoDBAttribute(attributeName = "payment_external_order_id")
    val externalOrderId: String,

    @Column(name = "payment_external_order_global_id")
    @field:DynamoDBAttribute(attributeName = "payment_external_order_global_id")
    val externalOrderGlobalId: String?,

    @field:DynamoDBAttribute(attributeName = "payment_payment_info")
    val paymentInfo: String,

    @field:DynamoDBAttribute(attributeName = "payment_created_at")
    @field:DynamoDBTypeConverted(converter = DynamoDBConfig.Companion.LocalDateTimeConverter::class)
    @field:DynamoDBIndexRangeKey(globalSecondaryIndexName = "payment_order_number_payment_created_at")
    val createdAt: LocalDateTime,

    @field:DynamoDBAttribute(attributeName = "payment_status")
    @field:DynamoDBIndexRangeKey(globalSecondaryIndexName = "payment_order_number_payment_status")
    val status: String,

    @field:DynamoDBAttribute(attributeName = "payment_status_changed_at")
    @field:DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    val statusChangedAt: LocalDateTime,
)
