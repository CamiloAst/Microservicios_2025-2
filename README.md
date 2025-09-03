# User Management API

Spring Boot 3 REST API for managing users with JWT security.

## Features
- Register and login users with JWT authentication
- CRUD operations for users (admin only)
- Forgot and reset password flows
- PostgreSQL database
- Swagger UI with OpenAPI 3.1
- Pagination for user listing
- Global error handling

## Running with Docker Compose

```
docker-compose up --build
```

Application will be available at `http://localhost:8080` and Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

Default PostgreSQL database is exposed on port `5432`.
