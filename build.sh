#!/bin/bash

# Build script for microservices

# Function to build a service
build_service() {
    echo "Building $1..."
    cd "$1" && rm -r target && mvn package -Dmaven.test.skip
    cd ..
    echo "Build completed for $1."
}

# Build service-discovery
build_service "service-discovery"

# Build application-gateway
build_service "application-gateway"

# Build comment-service
build_service "comment-service"

# Build config-server
build_service "config-server"

# Build notification-service
build_service "notification-service"

echo "All microservices built successfully."
