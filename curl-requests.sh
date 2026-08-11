#!/usr/bin/env bash

# Start the application before running these examples:
#   ./mvnw spring-boot:run

BASE_URL="http://localhost:8080"
USER_ID=1
MISSING_USER_ID=999999

# Create a valid user.
curl -i -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Example","email":"alice@example.com"}'

# Get all users.
curl -i "$BASE_URL/users"

# Get one user by ID. Change USER_ID to an ID in your database.
curl -i "$BASE_URL/users/$USER_ID"

# Update a user. PUT requires both name and email.
curl -i -X PUT "$BASE_URL/users/$USER_ID" \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Updated","email":"alice@example.com"}'

# Delete a user. A successful response is 204 No Content.
curl -i -X DELETE "$BASE_URL/users/$USER_ID"

# Invalid email -> 400 Bad Request.
curl -i -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{"name":"Invalid Email","email":"not-an-email"}'

# Blank name -> 400 Bad Request.
curl -i -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{"name":"   ","email":"blank-name@example.com"}'

# Duplicate email -> 409 Conflict.
# Run this first request once to create the duplicate-email example user,
# then repeat the same request to receive 409 Conflict.
curl -i -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{"name":"Duplicate Email","email":"duplicate@example.com"}'

curl -i -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{"name":"Duplicate Email","email":"duplicate@example.com"}'

# Nonexistent user -> 404 Not Found.
curl -i "$BASE_URL/users/$MISSING_USER_ID"
