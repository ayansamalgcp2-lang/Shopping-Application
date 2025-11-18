#!/bin/bash

echo "▶️  Starting All Docker Containers"
echo "================================="

# Get list of stopped containers
STOPPED=$(docker ps -aq --filter "status=exited")

if [ -z "$STOPPED" ]; then
    echo "ℹ️  No stopped containers found"
    exit 0
fi

echo "Starting containers..."
docker start $(docker ps -aq --filter "status=exited")

echo "✅ All containers started"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"