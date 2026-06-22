# FiscalCore API

FiscalCore is a REST API built with Java and Spring Boot for tax registration, authentication and tax calculation using JWT, MySQL and automated tests.

## Technologies

- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.4
- H2 (tests)
- JUnit 5 + Mockito
- Docker + Docker Compose

## Features

- User registration and login
- JWT-based authentication
- Tax CRUD endpoints
- Tax calculation endpoint
- Role-based authorization

## Environment Variables

See `.env.example` for all settings.

Main variables:

- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION`
- `SERVER_PORT`

## Run With Docker and MySQL

Build and run:

```bash
docker compose up --build
```

Or with Podman:

```bash
podman compose up --build
```

After startup:

- API: http://localhost:8080
- MySQL: localhost:3306

## Run Locally (without Docker)

```bash
./mvnw spring-boot:run
```

Use MySQL variables from `.env.example` or your shell environment.

## Main Endpoints

- `POST /auth/register`
- `POST /auth/login`
- `GET /taxes`
- `GET /taxes/{id}`
- `POST /taxes`
- `DELETE /taxes/{id}`
- `POST /tax-calculations`

## Run Tests

```bash
./mvnw test
```

Tests use H2 for speed and isolation.
