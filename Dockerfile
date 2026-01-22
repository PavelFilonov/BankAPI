FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/target/BankAPI-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
CMD ["java", "-jar", "BankAPI-0.0.1-SNAPSHOT.jar"]
