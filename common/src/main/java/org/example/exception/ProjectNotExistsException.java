package org.example.exception;

public class ProjectNotExistsException extends EntityNotExistsException {

    public ProjectNotExistsException(String message) {
        super(message);
    }
}
