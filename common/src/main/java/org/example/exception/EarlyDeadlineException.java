package org.example.exception;

public class EarlyDeadlineException extends RuntimeException {

    public EarlyDeadlineException(String message) {
        super(message);
    }
}
