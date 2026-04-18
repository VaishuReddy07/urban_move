package com.urban.urban_move.exception;

/**
 * Base exception for the application.
 */
public class UrbanMoveException extends RuntimeException {
    
    private final String errorCode;
    
    public UrbanMoveException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public UrbanMoveException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
