# # Multi-stage build for Spring Boot application
# FROM maven:3.9.9-eclipse-temurin-21 AS build

# WORKDIR /app

# # Copy pom.xml and download dependencies (cached layer)
# COPY pom.xml .
# RUN mvn dependency:go-offline -B

# # Copy source code and build
# COPY src ./src
# RUN mvn clean package -DskipTests

# # Production stage
# FROM eclipse-temurin:21-jre-alpine

# WORKDIR /app

# # Copy jar from build stage
# COPY --from=build /app/target/*.jar app.jar

# # Expose port
# EXPOSE 8080

# # Health check
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# # Run the application
# ENTRYPOINT ["java", "-jar", "app.jar"]


# syntax=docker/dockerfile:1.4

# ============================================
# Build stage - Using Maven Wrapper
# ============================================
FROM eclipse-temurin:21-jdk-alpine AS build

# Install dependencies for Maven wrapper
RUN apk add --no-cache bash

WORKDIR /app

# Copy Maven wrapper files first (better caching)
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (this layer is cached unless pom.xml changes)
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application with layertools enabled
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean package -DskipTests && \
    java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

# ============================================
# Production stage - Minimal runtime image
# ============================================
FROM eclipse-temurin:21-jre-alpine

# Install dumb-init for proper signal handling and wget for health checks
RUN apk add --no-cache dumb-init wget

WORKDIR /app

# Copy Spring Boot layers in order of change frequency
# Dependencies change least frequently, application code changes most
COPY --from=build /app/target/extracted/dependencies/ ./
COPY --from=build /app/target/extracted/spring-boot-loader/ ./
COPY --from=build /app/target/extracted/snapshot-dependencies/ ./
COPY --from=build /app/target/extracted/application/ ./

# Create non-root user for security
RUN addgroup -S spring && \
    adduser -S spring -G spring && \
    chown -R spring:spring /app

USER spring:spring

# Expose application port
EXPOSE 8080

# Health check for container orchestration
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run with dumb-init and optimized JVM settings
ENTRYPOINT ["dumb-init", "java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+UseStringDeduplication", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "org.springframework.boot.loader.launch.JarLauncher"]