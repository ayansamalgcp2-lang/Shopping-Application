#!/bin/sh

set -e

echo "========================================"
echo "🔧 Frontend Container Startup"
echo "========================================"
echo ""

# Validate BACKEND_URL
if [ -z "$BACKEND_URL" ]; then
    echo "❌ ERROR: BACKEND_URL is not set!"
    echo "   Please set it when deploying:"
    echo "   --set-env-vars BACKEND_URL=https://your-backend.run.app"
    exit 1
fi

echo "✅ BACKEND_URL: ${BACKEND_URL}"
echo ""

# Generate nginx config from template
echo "📝 Generating nginx configuration..."
envsubst '${BACKEND_URL}' < /etc/nginx/nginx.conf.template > /etc/nginx/conf.d/default.conf

# Verify config
echo ""
echo "📋 Nginx Configuration:"
echo "------------------------"
cat /etc/nginx/conf.d/default.conf
echo "------------------------"
echo ""

# Test nginx config
echo "🔍 Testing nginx configuration..."
nginx -t

echo ""
echo "🚀 Starting Nginx..."
echo ""

exec nginx -g 'daemon off;'