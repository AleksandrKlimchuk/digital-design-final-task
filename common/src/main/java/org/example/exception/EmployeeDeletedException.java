package org.example.exception;

public class EmployeeDeletedException extends RuntimeException{

    public EmployeeDeletedException(String message) {
        super(message);
    }
}
