#!/bin/bash

# check if postgres is running
if ! pg_isready -h localhost -p 5432 >/dev/null 2>/dev/null; then
    echo "Postgres is not running. Please start postgres and try again."
    exit 1
fi

# Config-Server
cd config-server && mvn spring-boot:run &

# wait till config server is up on http://localhost:8088/actuator/health
while ! curl -s http://localhost:8088/actuator/health | grep -q '{"status":"UP"}'; do
    echo "Waiting for config server to come up on port 8088..."
    sleep 5
done

# Service-Registry
cd service-discovery && mvn spring-boot:run -Dmaven.test.skip -Dspring-boot.run.profiles=profile1 -Dspring-boot.run.jvmArguments="-Dspring.application.name=service-registry -Dspring.config.import=configserver:http://localhost:8088" &

# wait till service registry is up on http://localhost:8761/actuator/health
while ! curl -s http://localhost:8761/actuator/health | grep -q '{"status":"UP"}'; do
    echo "Waiting for service registry to come up on port 8761..."
    sleep 5
done

# Application-Gateway
cd application-gateway && mvn spring-boot:run -Dmaven.test.skip -Dspring-boot.run.profiles=profile1 -Dspring-boot.run.jvmArguments="-Dspring.application.name=api-gateway -Dspring.config.import=configserver:http://localhost:8088" &

#wait till api gateway is up on http://localhost:8080/actuator/health
while ! curl -s http://localhost:8080/actuator/health | grep -q '{"status":"UP"}'; do
    echo "Waiting for api gateway to come up on port 8080..."
    sleep 5
done

# Comment-Services
cd comment-service && mvn spring-boot:run -Dmaven.test.skip -Dspring-boot.run.profiles=profile1 -Dspring-boot.run.jvmArguments="-Dspring.application.name=Comment-Service -Dspring.config.import=configserver:http://localhost:8088" &

# Notification-Services
cd notification-service && mvn spring-boot:run -Dmaven.test.skip -Dspring-boot.run.profiles=profile1 -Dspring-boot.run.jvmArguments="-Dspring.application.name=notification-service -Dspring.config.import=configserver:http://localhost:8088" &

echo "All microservices completed successfully."
