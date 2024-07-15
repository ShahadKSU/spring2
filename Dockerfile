# Use an official Maven image to build the app
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Expose port 8080
EXPOSE 8080

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/target/*.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]