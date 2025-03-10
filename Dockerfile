# Etapa de build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de execução (usando uma imagem melhor para Java)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia o JAR do build para o container final
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do Spring Boot (caso rode na 8080)
EXPOSE 8080

# Comando de execução
CMD ["java", "-jar", "app.jar"]
