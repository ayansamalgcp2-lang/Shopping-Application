#!/bin/bash

echo "🚀 Starting Full-Stack Shopping Application"
echo "==========================================="

# Start MongoDB
echo "1. Starting MongoDB..."   
docker-compose up -d
sleep 5

# Start Backend
echo "2. Starting Backend (in background)..."
cd product-service
./mvnw spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
echo "Backend PID: $BACKEND_PID"

# Wait for backend
echo "3. Waiting for backend to start..."
sleep 15

# Start Frontend
echo "4. Starting Frontend..."
cd ../frontend
npm start

echo ""
echo "✅ Application Started!"
echo "======================="
echo "Frontend: http://localhost:3000"
echo "Backend:  http://localhost:8080"
echo "MongoDB:  localhost:27017"