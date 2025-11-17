# 🛒 Shopping Application — Java Spring Boot Microservices 

A modern Shopping Application built with **Spring Boot** and **Angular 18**, following a **microservices architecture**.  
It demonstrates scalable backend design, secure authentication, real-time monitoring, and frontend integration using the latest tools.

---

## 🧩 Services Overview

- **🛍️ Product Service** – Manages products and categories  
- **🛒 Order Service** – Handles order creation and tracking  
- **📦 Inventory Service** – Maintains stock levels and warehouse data  
- **📩 Notification Service** – Sends emails/SMS on order events  
- **🌐 API Gateway** – Spring Cloud Gateway (MVC) for routing and request forwarding  
- **🛍️ Shop Frontend** – Built using Angular 18

---

## 🏗️ Tech Stack

**Backend**
- Java 17
- Spring Boot (Microservices)
- Spring Cloud Gateway MVC
- Kafka (Asynchronous messaging)
- MySQL & MongoDB (Databases)
- Redis (Caching)
- Keycloak (Authentication & Authorization)
- TestContainers & WireMock (Integration Testing)

**Frontend**
- Angular 18

**Monitoring & Observability**
- Prometheus (Metrics collection)
- Grafana (Dashboards & visualizations)
- Loki (Log aggregation)
- Tempo (Distributed Tracing)

**Deployment**
- Docker & Docker Compose
- Kubernetes (Orchestration)

---

## 🧱 Application Architecture

This application follows a **Domain-Driven, Microservices-based Architecture**:

- Each service is independently deployable and scalable  
- Communication between services uses **Kafka** and **REST APIs**  
- **Keycloak** secures APIs with role-based access  
- **API Gateway** handles routing, token validation, and acts as a single-entry point  
- Monitoring and logging are centralized using the **Grafana stack**  
- Integration testing supported via **TestContainers** and **WireMock**

---

## 📦 How to Run

```bash
# Clone the repository
git clone https://github.com/your-username/shopping-app.git

# Navigate into the project directory
cd shopping-app

# Start backend services
docker-compose up -d

# Start Angular frontend
cd shop-frontend
npm install
ng serve

#backend-service start

cd /mnt/c/Users/ayan/Downloads/gcp-experiments/Shopping-Application/product-service
./mvnw clean install -DskipTests
./mvnw spring-boot:run

# Without non-root user:
whoami
# Output: root

# If an attacker exploits your application:
# - They have root access inside the container
# - Potential to escape container and access host
# - Can modify system files
# - Can install malware

# With non-root user:
whoami
# Output: spring (UID 1000)

# If an attacker exploits your application:
# - Limited to "spring" user permissions
# - Cannot modify system files
# - Cannot install system packages
# - Reduced attack surface

# Inside container
$ whoami
spring

$ id
uid=1000(spring) gid=1000(spring) groups=1000(spring)

$ ls -la /app
drwxr-xr-x spring spring /app
-rw-r--r-- spring spring /app/application.jar

# Cannot do dangerous things:
$ apt-get install malware  # ❌ Permission denied
$ rm -rf /etc  # ❌ Permission denied

Scenario: SQL Injection vulnerability in your Spring Boot app
Running as RootRunning as Non-Root
❌ Attacker gets root shell✅ Attacker gets limited shell
❌ Can read /etc/shadow✅ Cannot read system files
❌ Can install backdoors✅ Cannot install packages
❌ Can modify app logs✅ Limited to app permissions
❌ Easier container escape✅ Harder to escalate privileges

It is trying to search about the mongodb inside the container but there is no mongodb inisde the backend container

# Just remove the corrupted cache layers , sometimes it might fail due to some reason
docker builder prune -f

# Build normally (will use cache for valid layers)
docker build -f Dockerfile2 -t backend:Dockerfile2 .

# Start MongoDB container
docker run -d \
  --name mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=admin123 \
  mongo:7.0

# Create a custom network
docker network create product-network

# Connect MongoDB to the network
docker network connect product-network mongodb

# Run your app connected to the same network
docker run -d \
  --name product-service \
  --network product-network \
  -p 8080:8080 \
  -e SPRING_DATA_MONGODB_HOST=mongodb \
  -e SPRING_DATA_MONGODB_PORT=27017 \
  -e SPRING_DATA_MONGODB_DATABASE=productdb \
  -e SPRING_DATA_MONGODB_USERNAME=admin \
  -e SPRING_DATA_MONGODB_PASSWORD=admin123 \
  backend:Dockerfile2