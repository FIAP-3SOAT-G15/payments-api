module "dynamodb_table" {
  source  = "terraform-aws-modules/dynamodb-table/aws"
  version = "4.0.1"
  
  name = "payments"
  hash_key = "payment_order_number"
  table_class = "STANDARD"
  
  attributes = [
    {
      name = "payment_order_number"
      type = "S"
    },
  ]

  tags = var.tags
}
