package com.urban.urban_move.exception;

/**
 * Exception thrown when authentication fails.
 */
public class AuthenticationException extends UrbanMoveException {
    
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_FAILED");
    }
}
