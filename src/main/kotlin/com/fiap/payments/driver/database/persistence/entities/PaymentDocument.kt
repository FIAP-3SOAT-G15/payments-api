package com.fiap.payments.driver.database.persistence.entities

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import com.fiap.payments.driver.database.configuration.DynamoDBConfig
import java.time.LocalDateTime

/**
 * Fields are nullable or receive blank String as default value,
 * otherwise it fails when trying to construct the object
 */
@DynamoDBTable(tableName = "payments")
class PaymentDocument(
    @field:DynamoDBHashKey
    @field:DynamoDBAttribute(attributeName = "payment_id")
    var id: String = "",

    @field:DynamoDBAttribute(attributeName = "payment_order_number")
    var orderNumber: String = "",

    @field:DynamoDBAttribute(attributeName = "payment_external_order_id")
    var externalOrderId: String? = null,

    @field:DynamoDBAttribute(attributeName = "payment_external_order_global_id")
    var externalOrderGlobalId: String? = null,

    @field:DynamoDBAttribute(attributeName = "payment_payment_info")
    var paymentInfo: String? = null,

    @field:DynamoDBAttribute(attributeName = "payment_created_at")
    @field:DynamoDBTypeConverted(converter = DynamoDBConfig.Companion.LocalDateTimeConverter::class)
    var createdAt: LocalDateTime? = null,

    @field:DynamoDBAttribute(attributeName = "payment_status")
    var status: String = "",

    @field:DynamoDBAttribute(attributeName = "payment_status_changed_at")
    @field:DynamoDBTypeConverted(converter = DynamoDBConfig.Companion.LocalDateTimeConverter::class)
    var statusChangedAt: LocalDateTime? = null,
)
