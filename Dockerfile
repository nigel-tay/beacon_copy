FROM node:18-alpine3.17 AS angbuilder

WORKDIR /app

COPY frontend/angular.json .
COPY frontend/package.json .
COPY frontend/package-lock.json .
COPY frontend/tsconfig.app.json .
COPY frontend/tsconfig.spec.json .
COPY frontend/tsconfig.json .
COPY frontend/ngsw-config.json .
COPY frontend/src src

RUN npm i -g @angular/cli
RUN npm ci
RUN ng build

FROM maven:3.8.5-openjdk-17 AS javabuilder

WORKDIR /app

COPY backend/src src
COPY backend/mvnw .
COPY backend/pom.xml .

COPY --from=angbuilder /app/dist/frontend /app/src/main/resources/static

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=javabuilder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

ARG RAILWAY_ENVIRONMENT
ENV RAILWAY_ENVIRONMENT=$RAILWAY_ENVIRONMENT

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar /app/app.jar