FROM gradle:7.6-eclipse-temurin-17 AS BUILD
WORKDIR /app
COPY . .
run gradle build --no-daemon
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/*.jar /app/usuario.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/usuario.jar"]