package com.urban.urban_move.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Vehicle responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    
    private Long id;
    
    private String plateNumber;
    
    private String vehicleType;
    
    private String make;
    
    private String model;
    
    private Integer year;
    
    private Integer capacity;
    
    private Double currentLatitude;
    
    private Double currentLongitude;
    
    private Double currentSpeed;
    
    private String status;
    
    private Double fuelLevel;
    
    private Integer totalDistance;
    
    private String driverName;
    
    private String fleetCode;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
