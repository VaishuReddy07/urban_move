package com.urban.urban_move.exception;

/**
 * Exception thrown when a resource is not found.
 */
public class ResourceNotFoundException extends UrbanMoveException {
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue),
            "RESOURCE_NOT_FOUND"
        );
    }
    
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
