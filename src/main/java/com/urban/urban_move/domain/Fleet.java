package com.urban.urban_move.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Fleet entity representing operational vehicle fleets.
 * Used for organizing vehicles and managing fleet-level operations.
 */
@Entity
@Table(name = "fleets", uniqueConstraints = @UniqueConstraint(columnNames = "fleetCode"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Fleet extends BaseEntity {
    
    @NotBlank(message = "Fleet name is required")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Fleet code is required")
    @Size(min = 2, max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String fleetCode;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Base location is required")
    @Column(nullable = false, length = 100)
    private String baseLocation;
    
    @Column(nullable = false)
    private Double baseLongitude;
    
    @Column(nullable = false)
    private Double baseLatitude;
    
    @Column(nullable = false)
    private Integer totalVehicles = 0;
    
    @Column(nullable = false)
    private Integer activeVehicles = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_manager_id")
    private User fleetManager;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FleetStatus status = FleetStatus.OPERATIONAL;
    
    @Column(nullable = false)
    private Double totalDistanceKm = 0.0;
    
    public enum FleetStatus {
        OPERATIONAL, MAINTENANCE, SUSPENDED, INACTIVE
    }
}
