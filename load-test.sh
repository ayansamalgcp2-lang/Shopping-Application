#!/bin/bash

# Configuration
URL="https://frontend-service-3gawdgzxeq-uc.a.run.app"
TOTAL_REQUESTS=200000
CONCURRENCY=100
TIMEOUT=1200  # 20 minutes

echo "╔════════════════════════════════════════╗"
echo "║         Load Testing Script            ║"
echo "╚════════════════════════════════════════╝"
echo ""
echo "🎯 Target: ${URL}"
echo "📊 Total Requests: ${TOTAL_REQUESTS}"
echo "⚡ Concurrency: ${CONCURRENCY}"
echo "⏱️  Timeout: ${TIMEOUT}s"
echo ""
echo "Press Enter to start..."
read

# Run the test
echo "🚀 Starting load test..."
echo ""

ab -n ${TOTAL_REQUESTS} \
   -c ${CONCURRENCY} \
   -k \
   -H "Connection: close" \
   ${URL}/

echo ""
echo "✅ Test complete!"

ab -n 200000 -c 100 \
  -k \
  -H "Connection: close" \
  https://frontend-service-3gawdgzxeq-uc.a.run.app/