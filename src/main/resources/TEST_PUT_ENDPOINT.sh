#!/bin/bash

# Test script for the new PUT endpoint to activate/deactivate subscriptions
# This script demonstrates how to use the new feature

echo "Testing PUT endpoint for subscription status updates"
echo "=================================================="

# Test data
ICAO_CODE="LDZA"
BASE_URL="http://localhost:8080"

echo "1. First, let's subscribe to an airport if not already subscribed"
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"icaoCode": "'$ICAO_CODE'"}' \
  $BASE_URL/subscriptions

echo -e "\n\n2. Check current subscriptions"
curl $BASE_URL/subscriptions

echo -e "\n\n3. Deactivate the subscription using PUT endpoint"
curl -X PUT \
  -H "Content-Type: application/json" \
  -d '{"active": "0"}' \
  $BASE_URL/subscriptions/$ICAO_CODE

echo -e "\n\n4. Check subscriptions again to see the change"
curl $BASE_URL/subscriptions

echo -e "\n\n5. Activate the subscription again using PUT endpoint"
curl -X PUT \
  -H "Content-Type: application/json" \
  -d '{"active": "1"}' \
  $BASE_URL/subscriptions/$ICAO_CODE

echo -e "\n\n6. Final check of subscriptions"
curl $BASE_URL/subscriptions

echo -e "\n\nTest complete!"