FROM openjdk:17

COPY target/  /usr/app/

WORKDIR /usr/app/

ENTRYPOINT ["java", "-jar", "application-gateway-0.0.1-SNAPSHOT.jar"]
