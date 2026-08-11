# Java Spring CRUD Demo

A Java 17 Spring Boot training demo implementing a User CRUD API with validation, MySQL persistence, and global exception handling.

## Tech stack

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA / Hibernate
- MySQL
- Maven Wrapper

## Configure MySQL

Create the `spring_demo` database, then export the connection settings before starting the application:

```bash
export DB_URL="jdbc:mysql://localhost:3306/spring_demo"
export DB_USERNAME="your_mysql_username"
export DB_PASSWORD="your_mysql_password"
```

## Run

```bash
./mvnw spring-boot:run
```

## Endpoints

- `POST /users` — create a user
- `GET /users` — list users
- `GET /users/{id}` — get a user
- `PUT /users/{id}` — update a user
- `DELETE /users/{id}` — delete a user

This is a Java/Spring Boot training demo.
