package org.example.exception;

public class EmployeeNotExistsException extends EntityNotExistsException {

    public EmployeeNotExistsException(String message) {
        super(message);
    }
}
