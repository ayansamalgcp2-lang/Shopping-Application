#!/bin/bash

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}"
echo "╔════════════════════════════════════════════╗"
echo "║  🐳 Shopping Application Docker Launcher  ║"
echo "║     Full-Stack Containerized Deployment   ║"
echo "╚════════════════════════════════════════════╝"
echo -e "${NC}"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker is not running!${NC}"
    echo "Starting Docker..."
    sudo service docker start
    sleep 5
fi

echo -e "${GREEN}✅ Docker is running${NC}"
echo ""

# Stop and remove existing containers
echo -e "${YELLOW}🛑 Stopping existing containers...${NC}"
docker-compose down 2>/dev/null

echo ""
echo -e "${BLUE}🏗️  Building and starting containers...${NC}"
echo -e "${YELLOW}This may take a few minutes on first run...${NC}"
echo ""

# Build and start all services
docker-compose up --build -d

# Wait for services to be ready
echo ""
echo -e "${CYAN}⏳ Waiting for services to be healthy...${NC}"
echo ""

# Function to check service health
check_service() {
    local service=$1
    local max_attempts=30
    local attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if docker-compose ps | grep $service | grep -q "healthy\|Up"; then
            echo -e "${GREEN}✅ $service is ready${NC}"
            return 0
        fi
        echo -e "${YELLOW}⏳ Waiting for $service... ($((attempt+1))/$max_attempts)${NC}"
        sleep 2
        ((attempt++))
    done
    
    echo -e "${RED}❌ $service failed to start${NC}"
    return 1
}

# Check each service
check_service "mongodb"
check_service "backend"
check_service "frontend"

echo ""
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo -e "${GREEN}    🎉 Application Started Successfully!    ${NC}"
echo -e "${CYAN}═══════════════════════════════════════════${NC}"
echo ""
echo -e "${BLUE}📊 Service Status:${NC}"
docker-compose ps
echo ""
echo -e "${CYAN}🌐 Access URLs:${NC}"
echo -e "  ${GREEN}Frontend:${NC}  http://localhost:3000"
echo -e "  ${GREEN}Backend:${NC}   http://localhost:8080/api/product"
echo -e "  ${GREEN}MongoDB:${NC}   localhost:27017"
echo ""
echo -e "${CYAN}📝 Useful Commands:${NC}"
echo -e "  View logs:        ${YELLOW}docker-compose logs -f${NC}"
echo -e "  Stop all:         ${YELLOW}docker-compose down${NC}"
echo -e "  Restart:          ${YELLOW}docker-compose restart${NC}"
echo -e "  View containers:  ${YELLOW}docker-compose ps${NC}"
echo ""
echo -e "${CYAN}═══════════════════════════════════════════${NC}"

# Open browser
echo -e "${BLUE}🌐 Opening browser...${NC}"
sleep 3
powershell.exe -Command "Start-Process msedge -ArgumentList 'http://localhost:3000'" 2>/dev/null || \
  explorer.exe "http://localhost:3000" 2>/dev/null || \
  echo -e "${YELLOW}Please open http://localhost:3000 manually${NC}"

echo ""
echo -e "${GREEN}✨ Enjoy your Shopping Application!${NC}"
