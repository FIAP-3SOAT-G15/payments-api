resource "aws_sns_topic" "payment_response_topic" {
  name = "payment-response_topic"
}

resource "aws_sns_topic_subscription" "request_payment_subscription" {
  topic_arn = aws_sns_topic.request_payment_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.request_payment_queue.arn

  raw_message_delivery = true
}

resource "aws_sns_topic" "request_payment_topic" {
  name = "request-payment_topic"
}

resource "aws_sns_topic_subscription" "payment_response_subscription" {
  topic_arn = aws_sns_topic.payment_response_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.payment_response_queue.arn

  raw_message_delivery = true
}