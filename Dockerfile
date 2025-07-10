## Dockerfile
#FROM eclipse-temurin:17-jdk AS build
#WORKDIR /app
#COPY . .
#RUN ./mvnw clean package -DskipTests
#
#FROM eclipse-temurin:17-jdk
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Étape 1 : build de l'application
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . ./
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Étape 2 : image de production
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/atelier-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
