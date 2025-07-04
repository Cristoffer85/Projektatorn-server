FROM maven:3-eclipse-temurin-21-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
COPY --from=build /target/*.jar demo.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo.jar"]