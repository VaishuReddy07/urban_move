package com.urban.urban_move.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for User responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    
    private Long id;
    
    private String username;
    
    private String email;
    
    private String fullName;
    
    private String phoneNumber;
    
    @JsonProperty("role")
    private String userRole;
    
    private Boolean emailVerified;
    
    private Boolean accountLocked;
    
    private String status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
