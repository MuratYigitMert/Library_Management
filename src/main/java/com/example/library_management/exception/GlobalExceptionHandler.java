package com.example.library_management.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String message = extractConstraintMessage(ex.getMessage());
        return buildResponseEntity(HttpStatus.CONFLICT, message, request.getDescription(false));
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions( WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", request.getDescription(false));
    }
    private String extractConstraintMessage(String fullMessage) {
        if (fullMessage.contains("users_username_key")) {
            return "Username already exists.";
        } else if (fullMessage.contains("users_email_key")) {
            return "Email already exists.";
        }
        return "Duplicate field value violates a constraint.";
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path.replace("uri=", "")); // cleans up URI path
        return new ResponseEntity<>(body, status);
    }
}
