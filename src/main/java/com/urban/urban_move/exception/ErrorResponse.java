package com.urban.urban_move.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error response DTO for API errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    
    private String timestamp;
    
    private int status;
    
    private String error;
    
    private String errorCode;
    
    private String message;
    
    private String path;
}
