package com.viktorvranar.airport_metar_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Global exception handler for the Airport METAR Service application.
 * Handles exceptions thrown by controllers and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle MetarDataNotFoundException and return a 404 Not Found response.
     *
     * @param ex the MetarDataNotFoundException
     * @param request the WebRequest
     * @return ResponseEntity with error details and HTTP 404 status
     */
    @ExceptionHandler(MetarDataNotFoundException.class)
    public ResponseEntity<Object> handleMetarDataNotFoundException(MetarDataNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle MethodArgumentNotValidException (validation errors) and return a 400 Bad Request response.
     *
     * @param ex the MethodArgumentNotValidException
     * @param request the WebRequest
     * @return ResponseEntity with error details and HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Validation failed");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        body.put("errors", errors);
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle generic exceptions and return a 500 Internal Server Error response.
     *
     * @param ex the Exception
     * @param request the WebRequest
     * @return ResponseEntity with error details and HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "An unexpected error occurred");
        body.put("details", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}