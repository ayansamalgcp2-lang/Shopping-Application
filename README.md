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

