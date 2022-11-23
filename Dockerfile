FROM node:14.21 AS frontend-dep
WORKDIR /app
COPY solarboat-app/package.json solarboat-app/package-lock.json  ./
RUN npm install
RUN npm install -g @angular/cli

FROM frontend-dep AS frontend-build
WORKDIR /app
COPY solarboat-app .
RUN ng build --prod


FROM maven:3.8.6-eclipse-temurin-11 AS backend-dep
WORKDIR /app
COPY solarboat/pom.xml .
RUN mvn install


FROM backend-dep as backend-build
WORKDIR /app
COPY solarboat/src src
COPY --from=frontend-build /app/dist src/main/resources/public/
RUN mvn --debug -f pom.xml package


FROM --platform=linux/amd64 eclipse-temurin:11.0.17_8-jre-alpine
ENV DEBIAN_FRONTEND=noninteractive
WORKDIR /app
COPY --from=backend-build /app/target/solarboat-0.0.1-SNAPSHOT.jar .

VOLUME /var/www/html
EXPOSE 8080

ENTRYPOINT [ "java","-Xmx2G","-jar", "solarboat-0.0.1-SNAPSHOT.jar" ]
