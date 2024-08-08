# REQUEST QUEUE

resource "aws_sqs_queue" "request_payment_dlq" {
  name = "request-payment_queue_dlq"
}

resource "aws_sqs_queue" "request_payment_queue" {
  name = "request-payment_queue"

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.request_payment_dlq.arn
    maxReceiveCount     = 3
  })

  delay_seconds = 5
}

# RESPONSE QUEUE

resource "aws_sqs_queue" "payment_response_dlq" {
  name = "payment-response_queue_dlq"
}

resource "aws_sqs_queue" "payment_response_queue" {
  name = "payment-response_queue"

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.payment_response_dlq.arn
    maxReceiveCount     = 3
  })

  delay_seconds = 5
}
