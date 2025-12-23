# Multi-stage build for backend_1
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
# Avoid baking secrets; provide real values at runtime via -e SPRING_DATA_MONGODB_URI=...
ENV SPRING_DATA_MONGODB_URI="mongodb://localhost:27017/foodapp_users" \
    SERVER_PORT=8080
COPY --from=build /app/target/backend_1-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
