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
 * Route entity representing optimized routes for vehicles.
 * Includes start/end locations and estimated travel metrics.
 */
@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Route extends BaseEntity {
    
    @NotBlank(message = "Route name is required")
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Starting location is required")
    @Column(nullable = false, length = 100)
    private String startLocation;
    
    @NotBlank(message = "Ending location is required")
    @Column(nullable = false, length = 100)
    private String endLocation;
    
    @NotNull(message = "Start latitude is required")
    @Column(nullable = false)
    private Double startLatitude;
    
    @NotNull(message = "Start longitude is required")
    @Column(nullable = false)
    private Double startLongitude;
    
    @NotNull(message = "End latitude is required")
    @Column(nullable = false)
    private Double endLatitude;
    
    @NotNull(message = "End longitude is required")
    @Column(nullable = false)
    private Double endLongitude;
    
    @NotNull(message = "Distance is required")
    @Min(0)
    @Column(nullable = false)
    private Double distanceKm;
    
    @NotNull(message = "Estimated duration is required")
    @Min(1)
    @Column(nullable = false)
    private Integer estimatedDurationMinutes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteStatus status = RouteStatus.PLANNED;
    
    @Column(columnDefinition = "LONGTEXT")
    private String routeGeometry;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    
    @Column(nullable = false)
    private Integer actualDurationMinutes = 0;
    
    @Column(nullable = false)
    private Double actualDistanceKm = 0.0;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    public enum RouteStatus {
        PLANNED, IN_PROGRESS, COMPLETED, CANCELLED, DELAYED
    }
}
