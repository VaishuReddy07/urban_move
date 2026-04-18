package com.urban.urban_move.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Fleet responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FleetDTO {
    
    private Long id;
    
    private String name;
    
    private String fleetCode;
    
    private String description;
    
    private String baseLocation;
    
    private Integer totalVehicles;
    
    private Integer activeVehicles;
    
    private String fleetManagerName;
    
    private String status;
    
    private Double totalDistanceKm;
}
