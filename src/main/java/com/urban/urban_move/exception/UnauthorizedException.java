package com.urban.urban_move.exception;

/**
 * Exception thrown when user is unauthorized for an operation.
 */
public class UnauthorizedException extends UrbanMoveException {
    
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}
