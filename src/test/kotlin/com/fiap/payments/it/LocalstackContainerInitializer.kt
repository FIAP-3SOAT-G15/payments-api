package com.fiap.payments.it

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.wait.strategy.Wait.forListeningPort
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.CreateTopicRequest
import software.amazon.awssdk.services.sns.model.SubscribeRequest
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest
import software.amazon.awssdk.services.sqs.model.QueueAttributeName

class LocalStackContainerInitializer :
    ApplicationContextInitializer<ConfigurableApplicationContext>,
    LocalStackContainer(
        DockerImageName.parse("localstack/localstack")
    ) {

    companion object {
        private val instance: LocalStackContainer = LocalStackContainerInitializer()
            .withServices(
                Service.DYNAMODB,
                Service.SQS,
                Service.SNS,
            )
            .withEnv(
                mapOf(
                    "DEBUG" to "1",
                    "DEFAULT_REGION" to "us-east-1",
                )
            )
            .waitingFor(forListeningPort())
    }

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        instance.start()

        val awsCredentials = AwsBasicCredentials.create(
            instance.accessKey,
            instance.secretKey
        )

        val region = Region.of(instance.region)

        val sqsClient = SqsClient.builder()
            .endpointOverride(instance.getEndpointOverride(Service.SQS))
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build()

        val snsClient = SnsClient.builder()
            .endpointOverride(instance.getEndpointOverride(Service.SNS))
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build()

        createQueues(sqsClient)
        createTopics(snsClient)
        subscribeToTopics(snsClient, sqsClient)

        TestPropertyValues.of(
            "spring.cloud.aws.endpoint=${instance.endpoint}",
            "spring.cloud.aws.credentials.access-key=${instance.accessKey}",
            "spring.cloud.aws.credentials.secret-key=${instance.secretKey}",
            "spring.cloud.aws.region.static=${instance.region}",
            "sns.topics.response-payment=${getTopicArn(snsClient, "payment-response_topic")}",
        ).applyTo(configurableApplicationContext)
    }

    private fun createQueues(sqsClient: SqsClient) {
        val queues = listOf("request-payment_queue", "payment-response_queue")

        queues.forEach { queue ->
            val dlqName = "${queue}_dlq"
            sqsClient.createQueue(CreateQueueRequest.builder().queueName(dlqName).build())
            println("DLQ queue [$dlqName] created")

            val dlqArn = getQueueArn(sqsClient, dlqName)

            val redrivePolicy = """{"deadLetterTargetArn":"$dlqArn","maxReceiveCount":"3"}"""

            sqsClient.createQueue(
                CreateQueueRequest.builder()
                    .queueName(queue)
                    .attributes(
                        mapOf(
                            QueueAttributeName.DELAY_SECONDS to "5",
                            QueueAttributeName.REDRIVE_POLICY to redrivePolicy
                        )
                    )
                    .build()
            )
            println("Queue [$queue] created")
        }
    }

    private fun createTopics(snsClient: SnsClient) {
        val topics = listOf("request-payment_topic", "payment-response_topic")

        topics.forEach { topic ->
            snsClient.createTopic(CreateTopicRequest.builder().name(topic).build())
            println("Topic [$topic] created")
        }
    }

    private fun getQueueArn(sqsClient: SqsClient, queueName: String) =
        sqsClient.getQueueAttributes(
            GetQueueAttributesRequest.builder()
                .queueUrl(sqsClient.getQueueUrl { it.queueName(queueName) }.queueUrl())
                .attributeNames(QueueAttributeName.QUEUE_ARN)
                .build()
        ).attributes()[QueueAttributeName.QUEUE_ARN]

    private fun getTopicArn(snsClient: SnsClient, topicName: String) =
        snsClient.createTopic { it.name(topicName) }.topicArn()

    private fun subscribeToTopics(snsClient: SnsClient, sqsClient: SqsClient) {
        val subscriptions = listOf(
            Pair("request-payment_topic", "request-payment_queue"),
            Pair("payment-response_topic", "payment-response_queue")
        )

        subscriptions.forEach { (topicName, queueName) ->
            val topicArn = getTopicArn(snsClient, topicName)
            val queueArn = getQueueArn(sqsClient, queueName)

            snsClient.subscribe(
                SubscribeRequest.builder()
                    .topicArn(topicArn)
                    .protocol("sqs")
                    .endpoint(queueArn)
                    .attributes(mapOf("RawMessageDelivery" to "true"))
                    .build()
            )
            println("Queue [$queueName] subscribed to Topic [$topicName]")
        }
    }
}
