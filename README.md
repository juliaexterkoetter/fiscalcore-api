# FiscalCore API

FiscalCore API is a Java Backend portfolio project built with Spring Boot. It provides user authentication, role-based access control, tax management and tax calculation using a clean REST API structure.

The project was designed to demonstrate backend development practices such as JWT authentication, layered architecture, relational database persistence, Docker-based local environment, automated tests and Swagger/OpenAPI documentation.

## Features

* User registration
* User login with JWT authentication
* Role-based access control with `USER` and `ADMIN`
* Automatic creation of default roles
* Automatic creation of a local admin user
* Tax creation
* Tax listing
* Tax lookup by ID
* Tax deletion
* Tax calculation using `BigDecimal`
* Global exception handling
* Swagger/OpenAPI documentation
* Docker Compose environment
* Automated tests with JUnit and Mockito

## Technologies

* Java 17
* Spring Boot 3
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* MySQL
* H2 Database for tests
* Maven
* Docker
* Docker Compose
* JUnit
* Mockito
* JaCoCo
* Swagger/OpenAPI

## Project Structure

```text
src/main/java/com/fiscalcore/api
├── config
├── controller
├── dto
├── exception
├── mapper
├── model
├── repository
├── security
└── service
```

### Main layers

* `config`: application configuration, security configuration, OpenAPI configuration and database initialization
* `controller`: REST endpoints
* `dto`: request and response objects
* `exception`: custom exceptions and global error handling
* `mapper`: conversion between entities and DTOs
* `model`: JPA entities
* `repository`: database access with Spring Data JPA
* `security`: JWT authentication and security filters
* `service`: business rules

## Authentication and Authorization

The API uses JWT authentication.

After logging in, the client receives a JWT token and must send it in protected requests using the following header:

```http
Authorization: Bearer <token>
```

### Default local admin user

For local development, the application creates a default admin user automatically:

```text
username: admin
password: admin123
```

These values can be changed using environment variables:

```env
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

## Roles

The project uses two roles:

```text
USER
ADMIN
```

### Public endpoints

```http
GET /health
POST /auth/register
POST /auth/login
GET /swagger-ui/**
GET /v3/api-docs/**
```

### Authenticated endpoints

```http
GET /taxes
GET /taxes/{id}
```

### Admin-only endpoints

```http
POST /taxes
DELETE /taxes/{id}
POST /tax-calculations
```

## Main Endpoints

### Health check

```http
GET /health
```

Example response:

```text
OK
```

### Register user

```http
POST /auth/register
```

Request:

```json
{
  "username": "john",
  "password": "password123"
}
```

### Login

```http
POST /auth/login
```

Request:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response:

```json
{
  "token": "jwt-token"
}
```

### Create tax

Requires `ADMIN`.

```http
POST /taxes
```

Request:

```json
{
  "name": "ICMS",
  "description": "Tax on goods circulation",
  "rate": 18.00
}
```

Response:

```json
{
  "id": 1,
  "name": "ICMS",
  "description": "Tax on goods circulation",
  "rate": 18
}
```

### List taxes

Requires authentication.

```http
GET /taxes
```

Response:

```json
[
  {
    "id": 1,
    "name": "ICMS",
    "description": "Tax on goods circulation",
    "rate": 18
  }
]
```

### Get tax by ID

Requires authentication.

```http
GET /taxes/{id}
```

Example:

```http
GET /taxes/1
```

### Delete tax

Requires `ADMIN`.

```http
DELETE /taxes/{id}
```

Example:

```http
DELETE /taxes/1
```

### Calculate tax

Requires `ADMIN`.

```http
POST /tax-calculations
```

Request:

```json
{
  "taxId": 1,
  "baseAmount": 1000.00
}
```

Response:

```json
{
  "taxName": "ICMS",
  "baseAmount": 1000,
  "taxRate": 18,
  "taxAmount": 180
}
```

## Environment Variables

Create a `.env` file based on `.env.example`.

```env
DATABASE_URL=jdbc:mysql://localhost:3306/fiscalcore?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DATABASE_USERNAME=fiscalcore_user
DATABASE_PASSWORD=fiscalcore_password

MYSQL_DATABASE=fiscalcore
MYSQL_USER=fiscalcore_user
MYSQL_PASSWORD=fiscalcore_password
MYSQL_ROOT_PASSWORD=root_password

JWT_SECRET=your-jwt-secret-at-least-32-characters-long
JWT_EXPIRATION=3600000

ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123

SERVER_PORT=8080
```

> Important: the `.env` file should not be committed to the repository.

## Running with Docker Compose

The project can be started with Docker Compose.

```bash
docker compose up -d
```

This starts:

```text
fiscalcore-mysql
fiscalcore-api
```

The API will be available at:

```text
http://localhost:8080
```

Swagger UI will be available at:

```text
http://localhost:8080/swagger-ui/index.html
```

To stop the containers:

```bash
docker compose down
```

## Running with Maven

If you want to run the API locally with Maven, make sure MySQL is running.

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

If Docker Compose is already running the API container, stop it before running the application with Maven to avoid port conflict on `8080`:

```bash
docker stop fiscalcore-api
```

## Swagger/OpenAPI

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON is available at:

```text
http://localhost:8080/v3/api-docs
```

The Swagger interface allows testing the API endpoints directly from the browser.

To test protected endpoints:

1. Log in using `POST /auth/login`
2. Copy the returned token
3. Click `Authorize`
4. Paste the token
5. Execute protected requests

## Running Tests

On Linux/macOS:

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

To run the full Maven verification lifecycle, including the JaCoCo report:

```bash
./mvnw clean verify
```

On Windows PowerShell:

```powershell
.\mvnw.cmd clean verify
```

## Test Coverage

The project includes automated tests for controllers, services, mappers, security and application context.

Current validated test result:

```text
Tests run: 20
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

## Validated Manual Flow

The following flow was manually validated using Swagger UI with the application running through Docker Compose:

```text
GET /health              -> 200 OK
POST /auth/login         -> 200 OK
POST /taxes              -> 201 Created
GET /taxes               -> 200 OK
POST /tax-calculations   -> 200 OK
```

Example tax calculation:

```text
Tax: ICMS
Rate: 18%
Base amount: 1000.00
Tax amount: 180.00
```

## Status

Portfolio-ready first version.

This project is actively evolving as part of a Java Backend portfolio.

## Future Improvements

* Add database migrations with Flyway
* Add CI with GitHub Actions
* Add pagination and filtering
* Add audit logs
* Add refresh token support
* Add integration tests with Testcontainers
* Improve Swagger annotations with detailed endpoint descriptions
* Add deployment configuration
* Add production-ready environment profiles

## About

FiscalCore API was built as a backend portfolio project focused on Java, Spring Boot, API security, database persistence, Docker and automated testing.
