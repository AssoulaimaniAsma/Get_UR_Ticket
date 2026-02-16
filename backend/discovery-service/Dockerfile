FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/*.jar discovery-service.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discovery-service.jar"]