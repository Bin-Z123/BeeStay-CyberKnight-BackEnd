# Tách build ra khỏi runtime
# - Image nhỏ gọn hơn (chỉ chứa jar)
# - Không chứa src code, Maven, Thư viện
# - Nhất quán trong môi trường CI/CD

# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Rumtime
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
