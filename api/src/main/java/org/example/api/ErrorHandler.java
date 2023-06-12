package org.example.api;

import org.example.dto.ErrorDTO;
import org.example.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotExistsException.class)
    public ResponseEntity<ErrorDTO> notFoundErrorHandler(EntityNotExistsException e) {
        return errorHandler(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({EmployeeDeletedException.class, EntityAlreadyExistsException.class})
    public ResponseEntity<ErrorDTO> conflictErrorHandler(RuntimeException e) {
        return errorHandler(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({EarlyDeadlineException.class})
    public ResponseEntity<ErrorDTO> badRequestHandler(RuntimeException e) {
        return errorHandler(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<ErrorDTO> errorHandler(HttpStatus status, Exception exception) {
        return ResponseEntity.status(status).body(new ErrorDTO(exception.getMessage()));
    }
}
