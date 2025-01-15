# Base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy your Spring Boot jar file
COPY target/texterApp-0.0.1-SNAPSHOT.jar /app/texter-app.jar

# Install PostgreSQL client for convenience if needed
RUN apt-get update && apt-get install -y postgresql-client

# Expose PostgreSQL and Spring Boot application ports
EXPOSE 5432 8080

# Start the Spring Boot application
CMD java -jar texter-app.jar
