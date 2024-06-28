FROM openjdk:17

ARG JAR_FILE=./ChargeControlBackend/build/libs/ChargeControlServer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} chargecontrol.jar
ENTRYPOINT ["java","-jar","chargecontrol.jar"]
