package com.urban.urban_move.controller;

import com.urban.urban_move.exception.ErrorResponse;
import com.urban.urban_move.exception.ResourceAlreadyExistsException;
import com.urban.urban_move.exception.ResourceNotFoundException;
import com.urban.urban_move.exception.AuthenticationException;
import com.urban.urban_move.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.access.AccessDeniedException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .errorCode(ex.getErrorCode())
            .message(ex.getMessage())
            .path(request.getServletPath())
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(
        ResourceAlreadyExistsException ex, HttpServletRequest request) {
        log.error("Resource already exists: {}", ex.getMessage());
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpStatus.CONFLICT.value())
            .error("Conflict")
            .errorCode(ex.getErrorCode())
            .message(ex.getMessage())
            .path(request.getServletPath())
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
        AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication failed: {}", ex.getMessage());
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .errorCode(ex.getErrorCode())
            .message(ex.getMessage())
            .path(request.getServletPath())
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
        UnauthorizedException ex, HttpServletRequest request) {
        log.error("User unauthorized: {}", ex.getMessage());
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpStatus.FORBIDDEN.value())
            .error("Forbidden")
            .errorCode(ex.getErrorCode())
            .message(ex.getMessage())
            .path(request.getServletPath())
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
        AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied: {}", ex.getMessage());
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpStatus.FORBIDDEN.value())
            .error("Forbidden")
            .errorCode("ACCESS_DENIED")
            .message("Access denied to this resource")
            .path(request.getServletPath())
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        log.error("Validation error: {}", message);
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .errorCode("VALIDATION_ERROR")
            .message(message)
            .path(request.getServletPath())
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);
        
        String timestamp = Instant.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(timestamp)
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .errorCode("INTERNAL_ERROR")
            .message("An unexpected error occurred. Please try again later.")
            .path(request.getServletPath())
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
