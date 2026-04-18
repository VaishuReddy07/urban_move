package com.urban.urban_move.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO for creating/updating vehicles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVehicleRequest {
    
    @NotBlank(message = "Plate number is required")
    private String plateNumber;
    
    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    @Min(1900)
    private Integer year;
    
    @NotNull(message = "Capacity is required")
    @Min(1)
    private Integer capacity;
    
    @NotNull(message = "Latitude is required")
    private Double currentLatitude;
    
    @NotNull(message = "Longitude is required")
    private Double currentLongitude;
    
    private Long driverId;
    
    private Long fleetId;
}
