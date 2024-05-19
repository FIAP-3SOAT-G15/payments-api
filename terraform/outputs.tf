output "mercado_pago_secrets_read_only_policy_arn" {
  description = "The ARN of the Mercado Pago secrets"
  value       = aws_iam_policy.mercado_pago_secrets_read_only_policy.arn
}

output "payments_dynamodb_table_policy_arn" {
  description = "The ARN of the DynamoDB table for payments"
  value       = aws_iam_policy.payments_dynamodb_table_policy.arn
}
