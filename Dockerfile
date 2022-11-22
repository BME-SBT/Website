FROM node:14.21 AS frontend-build
WORKDIR /usr/src/app
COPY solarboat-app/package.json solarboat-app/package-lock.json  ./
RUN npm install
COPY solarboat-app .
RUN npm install -g @angular/cli
RUN ng build --prod


FROM maven:3.6.3-jdk-8 AS backend
ENV DEBIAN_FRONTEND=noninteractive
RUN apt update && apt install -y mariadb-server

WORKDIR /home/app
COPY solarboat/pom.xml .
RUN mvn install

COPY solarboat/init.sql .
RUN /etc/init.d/mysql start && mysql < init.sql 

COPY solarboat/src src
COPY --from=frontend-build /usr/src/app/dist src/main/resources/public/
RUN /etc/init.d/mysql start && mvn --debug -f pom.xml package

VOLUME /var/www/html


EXPOSE 8080

VOLUME /var/lib/mysql

CMD /etc/init.d/mysql start && java -jar target/solarboat-0.0.1-SNAPSHOT.jar
