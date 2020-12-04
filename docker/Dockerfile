FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE
ARG APP_PROP_LOC
COPY ${JAR_FILE} app.jar
COPY ${APP_PROP_LOC} app.properties

ENTRYPOINT "java" \
    "-jar" \
    "-Dspring.config.location=app.properties" \
    "/app.jar"
