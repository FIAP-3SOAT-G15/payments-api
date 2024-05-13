package com.fiap.payments.driver.database.persistence.repository

import com.fiap.payments.driver.database.persistence.entities.PaymentDocument
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface PaymentDynamoRepository : CrudRepository<PaymentDocument, Long>
