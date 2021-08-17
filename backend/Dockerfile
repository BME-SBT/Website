# Build the server from sources
FROM maven:3-jdk-8 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn install

COPY ./ .
RUN mvn -f pom.xml package

#TODO: Run tests
# FROM openjdk:....
# RUN mvn istall -DskipTests=false

# Runtime image, jdk and built jar only
FROM openjdk:8-jre-slim as runtime
WORKDIR /app
COPY --from=builder /app/target/solarboat-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]