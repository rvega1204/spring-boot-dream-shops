package com.rvega.dreamshops.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * This class serves as a global exception handler for the application.
 * It provides custom handling for exceptions that may occur throughout the application.
 *
 * @author rvega
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AccessDeniedException exceptions.
     * This exception is thrown when a user tries to access a resource they do not have permission to.
     *
     * @param ex The AccessDeniedException that occurred.
     * @return A ResponseEntity containing a message indicating the user does not have permission to access the resource,
     *         and a HTTP status code of 403 (FORBIDDEN).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        String message = "You don't have permission to this action.";
        return new ResponseEntity<>(message, FORBIDDEN);
    }
}
