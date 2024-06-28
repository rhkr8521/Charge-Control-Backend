# Dockerfile

# Use JDK 17 as the base image
FROM openjdk:17

# Set the argument for the JAR file location
ARG JAR_FILE=build/libs/ChargeControlServer-0.0.1-SNAPSHOT.jar

# Add the JAR file to the Docker image
ADD ${JAR_FILE} chargecontrol.jar

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "chargecontrol.jar"]
