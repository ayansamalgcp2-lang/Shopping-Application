#!/bin/bash

echo "🛑 Stopping All Docker Containers"
echo "================================="

# Get list of running containers
RUNNING=$(docker ps -q)

if [ -z "$RUNNING" ]; then
    echo "ℹ️  No containers are running"
    exit 0
fi

echo "Stopping containers..."
docker stop $(docker ps -q)

echo "✅ All containers stopped"
docker ps -a --format "table {{.Names}}\t{{.Status}}"