module "dynamodb_table" {
  source  = "terraform-aws-modules/dynamodb-table/aws"
  version = "4.0.1"

  name        = "payments"
  hash_key    = "payment_order_number"
  range_key   = "payment_created_at"
  table_class = "STANDARD"

  attributes = [
    {
      name = "payment_order_number"
      type = "S"
    },
    {
      name = "payment_created_at"
      type = "S"
    }
  ]

  tags = var.tags
}
