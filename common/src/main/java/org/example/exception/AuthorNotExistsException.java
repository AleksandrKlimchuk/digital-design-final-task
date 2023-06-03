package org.example.exception;

public class AuthorNotExistsException extends EmployeeNotExistsException {

    public AuthorNotExistsException(String message) {
        super(message);
    }
}
