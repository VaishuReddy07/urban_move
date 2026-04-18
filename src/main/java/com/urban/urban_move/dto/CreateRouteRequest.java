package com.urban.urban_move.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO for creating/updating routes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRouteRequest {
    
    @NotBlank(message = "Route name is required")
    private String name;
    
    @NotBlank(message = "Start location is required")
    private String startLocation;
    
    @NotBlank(message = "End location is required")
    private String endLocation;
    
    @NotNull(message = "Start latitude is required")
    private Double startLatitude;
    
    @NotNull(message = "Start longitude is required")
    private Double startLongitude;
    
    @NotNull(message = "End latitude is required")
    private Double endLatitude;
    
    @NotNull(message = "End longitude is required")
    private Double endLongitude;
    
    @NotNull(message = "Distance is required")
    @Min(0)
    private Double distanceKm;
    
    @NotNull(message = "Duration is required")
    @Min(1)
    private Integer estimatedDurationMinutes;
    
    private Long vehicleId;
}
