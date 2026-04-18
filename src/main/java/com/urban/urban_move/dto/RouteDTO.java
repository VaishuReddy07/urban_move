package com.urban.urban_move.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Route responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDTO {
    
    private Long id;
    
    private String name;
    
    private String startLocation;
    
    private String endLocation;
    
    private Double startLatitude;
    
    private Double startLongitude;
    
    private Double endLatitude;
    
    private Double endLongitude;
    
    private Double distanceKm;
    
    private Integer estimatedDurationMinutes;
    
    private Integer actualDurationMinutes;
    
    private Double actualDistanceKm;
    
    private String status;
    
    private String vehiclePlateNumber;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
