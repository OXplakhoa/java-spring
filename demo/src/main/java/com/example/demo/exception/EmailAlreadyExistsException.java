package com.example.demo.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("A user with email " + email + " already exists");
    }

    public EmailAlreadyExistsException(String email, Throwable cause) {
        super("A user with email " + email + " already exists", cause);
    }
}
