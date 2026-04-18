package com.urban.urban_move.controller;

import com.urban.urban_move.exception.ResourceNotFoundException;
import com.urban.urban_move.exception.ResourceAlreadyExistsException;
import com.urban.urban_move.exception.AuthenticationException;
import com.urban.urban_move.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Exception handler for web controllers (returns HTML views).
 * Handles all exceptions thrown by @Controller and @RestController classes.
 */
@ControllerAdvice
@Slf4j
public class WebExceptionHandler {
    
    private ModelAndView createErrorView(HttpStatus status, String error, String message, String path) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", status.value());
        mav.addObject("error", error);
        mav.addObject("message", message != null ? message : "An error occurred");
        mav.addObject("path", path);
        mav.setStatus(status);
        return mav;
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(
        ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        return createErrorView(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request.getServletPath());
    }
    
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ModelAndView handleResourceAlreadyExistsException(
        ResourceAlreadyExistsException ex, HttpServletRequest request) {
        log.error("Resource already exists: {}", ex.getMessage());
        return createErrorView(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request.getServletPath());
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(
        AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication failed: {}", ex.getMessage());
        return createErrorView(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), request.getServletPath());
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(
        UnauthorizedException ex, HttpServletRequest request) {
        log.error("User unauthorized: {}", ex.getMessage());
        return createErrorView(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage(), request.getServletPath());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(
        AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied: {}", ex.getMessage());
        return createErrorView(HttpStatus.FORBIDDEN, "Access Denied", 
            "You do not have permission to access this resource", request.getServletPath());
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpServletRequest request) {
        log.error("Page not found: {}", ex.getRequestURL());
        return createErrorView(HttpStatus.NOT_FOUND, "Page Not Found", 
            "The requested page does not exist", request.getServletPath());
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNullPointerException(
        NullPointerException ex, HttpServletRequest request) {
        log.error("Null pointer exception", ex);
        return createErrorView(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", 
            "A system error occurred. Please contact support if the problem persists.", request.getServletPath());
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(
        Exception ex, HttpServletRequest request) {
        log.error("An unexpected error occurred", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred. Please try again later.";
        return createErrorView(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", message, request.getServletPath());
    }
}
