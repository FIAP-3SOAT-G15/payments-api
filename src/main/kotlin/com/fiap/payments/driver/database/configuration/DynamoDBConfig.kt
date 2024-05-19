package com.fiap.payments.driver.database.configuration

import com.amazonaws.auth.WebIdentityTokenCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.fiap.payments.driver.database.persistence.repository"])
class DynamoDBConfig {

    @Primary
    @Bean
    fun dynamoDBMapper(amazonDynamoDB: AmazonDynamoDB): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT)
    }

    /**
     * For local run.
     */
    @Bean("amazonDynamoDB")
    @ConditionalOnProperty("aws.dynamodb.local", havingValue = "true")
    fun amazonDynamoDB(
        @Value("\${aws.dynamodb.endpoint}") endpoint: String,
        @Value("\${aws.dynamodb.region}") region: String,
    ): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder.standard()
            // using default credentials provider chain, which searches for environment variables
            // AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
            // and therefore only endpoint override is needed
            .withEndpointConfiguration(EndpointConfiguration(endpoint, region))
            .build()
    }

    /**
     * For production run.
     * 
     * This is not necessary when using AWS SDK v2.
     * However, since we are using this old library
     * https://github.com/boostchicken/spring-data-dynamodb
     * we need to keep as v1 and include token provider to the chain.
     */
    @Bean("amazonDynamoDB")
    @ConditionalOnProperty("aws.dynamodb.local", havingValue = "false")
    fun awsCredentialsProvider(): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder.standard()
            // AWS_WEB_IDENTITY_TOKEN_FILE is present.
            // This environment variable is included in AWS SDK v2, but not AWS SDK v1.
            // Since project uses this old library
            // https://github.com/boostchicken/spring-data-dynamodb
            // it is needed to keep v1 and include token provider to the chain.
            .withCredentials(WebIdentityTokenCredentialsProvider.builder().build())
            .build()
    }

    companion object {
        class LocalDateTimeConverter : DynamoDBTypeConverter<Date, LocalDateTime> {
            override fun convert(source: LocalDateTime): Date {
                return Date.from(source.toInstant(ZoneOffset.UTC))
            }

            override fun unconvert(source: Date): LocalDateTime {
                return source.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
            }
        }
    }
}
