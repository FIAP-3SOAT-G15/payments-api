#!/usr/bin/env bash

set -e

export AWS_ACCESS_KEY_ID=fakekey
export AWS_SECRET_ACCESS_KEY=fakeaccesskey
export AWS_REGION=us-east-2

aws dynamodb create-table \
  --table-name payments \
  --attribute-definitions \
      AttributeName=payment_order_number,AttributeType=S \
  --key-schema \
      AttributeName=payment_order_number,KeyType=HASH \
  --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=10 \
  --endpoint-url http://localhost:54000 \
  --region us-east-1 || true | cat