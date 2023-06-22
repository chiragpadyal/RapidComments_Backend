# Rapid Comment Backend

## Description

Rapid Comment is a comment service that can be used in any web application. It is a microservice based architecture that can be deployed in any cloud environment. It is built with spring boot and spring cloud.

## Getting Started

### Prerequisites

#### local

- Java 17
- Maven 3.8.2
- Postgres
- Rabbitmq
- Zipkin

#### docker

- Docker
- docker-compose

### Installation Locally

- Clone the repository

```
git clone https://github.com/chiragpadyal/RapidComments_Backend
```

- Run the project

```sh
bash run.sh
```

### Installation with Docker

- Clone the repository

```
git clone https://github.com/chiragpadyal/RapidComments_Backend
```

- Run docker-compose

```
docker-compose up
```

## Architecture Design

<!-- add image of url rapidcomments-drawio.jpg -->

![Rapid Comments Architecture](./rapidcomments-drawio.jpg "Rapid Comments Architecture")

## Features

- Microservice based architecture
- Load balanced with eureka service discovery
- Mail dilevery with rabbitmq
- Dockerized
- Zipkin log tracing
