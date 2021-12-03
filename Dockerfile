# syntax=docker/dockerfile:1

FROM maven:3.8.1-openjdk-17

WORKDIR /app

COPY pom.xml ./
RUN mvn clean package
COPY src ./src

