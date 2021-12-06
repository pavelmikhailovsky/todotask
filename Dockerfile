# syntax=docker/dockerfile:1

FROM maven:3.8.1-openjdk-17 as base

WORKDIR /app
#TODO fix compile code
COPY pom.xml ./
COPY src ./src
CMD ["mvn", "clean", "package"]

FROM base as test
RUN ["mvn", "test"]

FROM base as development

WORKDIR /app

COPY --from=base /app/pom.xml /app/pom.xml
COPY --from=base /app/src /app/src
