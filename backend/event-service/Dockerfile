# event-service/Dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copier le jar
COPY target/*.jar app.jar

# ✅ Créer le dossier pour H2 et copier la base de données
RUN mkdir -p /h2-database

# Copier les fichiers H2 dans le conteneur
COPY src/main/resources/h2-data/eventdb.mv.db /h2-database/eventdb.mv.db
COPY src/main/resources/h2-data/eventdb.trace.db /h2-database/eventdb.trace.db

EXPOSE 7071

ENTRYPOINT ["java", "-jar", "app.jar"]