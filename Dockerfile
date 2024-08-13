
# Use a base image compatible with linux/amd64 and Java 17
FROM openjdk:17-slim-buster
# Maintainer of the image
MAINTAINER Santhoshkumar-Ravi
# Copy the JAR file into the container
COPY target/banking-0.0.1.jar banking-0.0.1.jar
# Specify the entry point for the application
ENTRYPOINT ["java","-jar","/banking-0.0.1.jar"]