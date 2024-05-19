module "dynamodb_table" {
  source  = "terraform-aws-modules/dynamodb-table/aws"
  version = "4.0.1"

  name        = "payments"
  hash_key    = "payment_order_number"
  table_class = "STANDARD"

  attributes = [
    {
      name = "payment_order_number"
      type = "S"
    },
  ]

  tags = var.tags
}

resource "aws_iam_policy" "payments_dynamodb_table_policy" {
  name = "TechChallengePaymentsDynamoDBTablePolicy"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        "Sid" : "ListAndDescribe",
        "Effect" : "Allow",
        "Action" : [
          "dynamodb:List*",
          "dynamodb:DescribeReservedCapacity*",
          "dynamodb:DescribeLimits",
          "dynamodb:DescribeTimeToLive"
        ],
        "Resource" : "*"
      },
      {
        Effect = "Allow"
        Action = [
          "dynamodb:BatchGet*",
          "dynamodb:DescribeStream",
          "dynamodb:DescribeTable",
          "dynamodb:Get*",
          "dynamodb:Query",
          "dynamodb:Scan",
          "dynamodb:BatchWrite*",
          "dynamodb:CreateTable",
          "dynamodb:Delete*",
          "dynamodb:Update*",
          "dynamodb:PutItem"
        ],
        Resource = module.dynamodb_table.dynamodb_table_arn
      }
    ]
  })
}
