FROM openjdk:23-jdk
LABEL org.opencontainers.image.authors="rvarttinen@autocorrect.se"
COPY build/libs/spring-example-magic-all-0.0.1-SNAPSHOT.jar spring-example-magic-all-0.0.1-SNAPSHOT.jar

USER 0
RUN mkdir -p -v /MyDatabases/DB1
RUN ls -las
USER $CONTAINER_USER_ID

ENTRYPOINT ["java", "-Dspring.profiles.active=test", "-jar", "/spring-example-magic-all-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080/tcp 8080/udp