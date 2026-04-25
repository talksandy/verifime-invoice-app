# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/quarkus-app/ /app/

EXPOSE 8080

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"

CMD ["java", "-jar", "quarkus-run.jar"]