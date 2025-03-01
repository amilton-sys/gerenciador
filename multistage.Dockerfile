FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM alpine:3.17
RUN apk add -no-cache openjdk17-jre
WORKDIR /app
ENV JAR_NAME=gerenciar.jar
COPY --from=build /app/target/$JAR_NAME $JAR_NAME
CMD java -jar $JAR_NAME
