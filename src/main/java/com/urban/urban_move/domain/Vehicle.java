package com.urban.urban_move.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

/**
 * Vehicle entity representing connected vehicles in the mobility network.
 * Includes real-time location and status tracking.
 */
@Entity
@Table(name = "vehicles", uniqueConstraints = @UniqueConstraint(columnNames = "plateNumber"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Vehicle extends BaseEntity {
    
    @NotBlank(message = "Vehicle plate number is required")
    @Column(nullable = false, unique = true, length = 20)
    private String plateNumber;
    
    @NotBlank(message = "Vehicle type is required")
    @Column(nullable = false, length = 50)
    private String vehicleType;
    
    @NotBlank(message = "Make is required")
    @Column(nullable = false, length = 50)
    private String make;
    
    @NotBlank(message = "Model is required")
    @Column(nullable = false, length = 50)
    private String model;
    
    @NotNull(message = "Year is required")
    @Min(1900)
    @Column(nullable = false)
    private Integer year;
    
    @NotNull(message = "Capacity is required")
    @Min(1)
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false)
    private Double currentLatitude;
    
    @Column(nullable = false)
    private Double currentLongitude;
    
    @Column(nullable = false)
    private Double currentSpeed = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.IDLE;
    
    @Column(nullable = false)
    private Double fuelLevel = 100.0;
    
    @Column(nullable = false)
    private Integer totalDistance = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;
    
    @Column(nullable = false)
    private Long lastLocationUpdateTime;
    
    public enum VehicleStatus {
        IDLE, IN_SERVICE, CHARGING, MAINTENANCE, ACTIVE, OFFLINE
    }
}
