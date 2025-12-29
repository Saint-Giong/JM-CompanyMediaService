# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy root + module pom(s) first for better layer caching
COPY pom.xml ./
COPY CompanyMediaApi/pom.xml CompanyMediaApi/pom.xml
COPY CompanyMediaService/pom.xml CompanyMediaService/pom.xml

# Copy Maven wrapper (root)
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd

# Download dependencies (best-effort cache warm up)
RUN mvn -q -DskipTests dependency:go-offline

# Copy the actual sources (only the modules we need)
COPY CompanyMediaApi/src/ CompanyMediaApi/src/
COPY CompanyMediaService/src/ CompanyMediaService/src/

# Build only the service module (will also build required modules)
RUN mvn -q -DskipTests -pl CompanyMediaService -am package

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Spring Boot listens on 8081 per compose mapping
EXPOSE 8081

# Copy the built jar
COPY --from=build /workspace/CompanyMediaService/target/*.jar /app/app.jar

# Allow passing JVM opts via env
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
