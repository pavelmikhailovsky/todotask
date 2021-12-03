# syntax=docker/dockerfile:1

FROM maven:3.8.1-openjdk-17 as base

WORKDIR /app

COPY pom.xml ./
RUN mvn clean package
COPY src ./src

FROM base as test
RUN mvn test

FROM base as development
CMD ["mvn", "cargo:run", "-Drun.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000'"]

FROM base as build
RUN mvn package

FROM base as production
EXPOSE 8080

COPY --from=build /app/target/todotask-*.war /todotask.war

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/todotask.war"]


