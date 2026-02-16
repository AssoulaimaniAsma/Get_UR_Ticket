FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/*.jar gateway-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]