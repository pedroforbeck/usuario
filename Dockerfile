FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY . .
RUN chmod +x gradlew
RUN ./gradlew build --no-daemon -x test

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/usuario.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/usuario.jar"]