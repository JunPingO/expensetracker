# client
FROM node:19 AS client

WORKDIR /app

COPY frontend .

# RUN cd client
RUN npm install -g @angular/cli
RUN npm i
RUN ng build

# server
FROM maven:3.9.0-eclipse-temurin-19 AS backend

WORKDIR /app

COPY backend/mvnw .
COPY backend/mvnw.cmd .
COPY backend/pom.xml .
COPY backend/src ./src
COPY --from=frontend /app/dist/frontend ./src/main/resources/static

RUN mvn package -Dmaven.test.skip=true


# copy jar over
FROM eclipse-temurin:19-jre

WORKDIR /app

COPY --from=backend app/target/server-0.0.1-SNAPSHOT.jar server.jar

ENV PORT=8080
ENV SPRING_DATA_MONGODB_HOST=""
ENV SPRING_DATA_MONGODB_PORT=""
ENV SPRING_DATA_MONGODB_USERNAME=""
ENV SPRING_DATA_MONGODB_PASSWORD=""
ENV SPRING_DATA_MONGODB_AUTHENTICATION-DATABASE=""
ENV SPRING_DATA_MONGODB_DATABASE=""
ENV JWT_KEY=""
ENV SPRING_MAIL_HOST=smtp.gmail.com
ENV SPRING_MAIL_PORT=587
ENV SPRING_MAIL_PASSWORD=""
ENV SPRING_MAIL_USERNAME=""
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true

EXPOSE ${PORT}

ENTRYPOINT java -Dserver.port=${PORT} -jar server.jar


