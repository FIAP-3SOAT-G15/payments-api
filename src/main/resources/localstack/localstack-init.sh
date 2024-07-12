#!/bin/bash

LOCALSTACK_HOST=localhost

echo "Creating SQS queues..."

SQS_QUEUES=("request-payment_queue" "payment-response_queue")

for queue in "${SQS_QUEUES[@]}"
do
  aws sqs create-queue \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --queue-name="${queue}_dlq"
  echo "DLQ queue [${queue}_dlq] created"

  aws sqs create-queue \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --queue-name=$queue \
      --attributes DelaySeconds=5,RedrivePolicy="\"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:us-west-2:000000000000:${queue}_dlq\\\",\\\"maxReceiveCount\\\":\\\"3\\\"}\""
  echo "Queue [$queue] created"
done

echo "Creating SNS topics..."

SNS_TOPICS=("request-payment_topic" "payment-response_topic")

for topic in "${SNS_TOPICS[@]}"
do
  aws sns create-topic \
      --endpoint-url=http://$LOCALSTACK_HOST:4566 \
      --name=$topic
  echo "Topic [$topic] created"
done


aws sns subscribe \
      --endpoint-url=http://localhost:4566 \
      --topic-arn=arn:aws:sqs:us-east-2:000000000000:request-payment_topic  \
      --protocol=sqs \
      --notification-endpoint=arn:aws:sns:us-east-2:000000000000:request-payment_queue

echo "Topic request-payment_queue subscribe"

aws sns subscribe \
      --endpoint-url=http://localhost:4566 \
      --topic-arn=arn:aws:sqs:us-east-2:000000000000:payment-response_topic \
      --protocol=sqs \
      --notification-endpoint=arn:aws:sns:us-east-2:000000000000:payment-response_queue

echo "Topic payment-response_queue subscribe"

echo "Configuration completed, ready to dev!"