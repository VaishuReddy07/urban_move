package com.urban.urban_move.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * MobilityEvent entity for capturing real-time transportation events.
 * Includes vehicle movements, traffic conditions, and system events.
 */
@Entity
@Table(name = "mobility_events", indexes = {
    @Index(name = "idx_vehicle_timestamp", columnList = "vehicle_id,event_timestamp"),
    @Index(name = "idx_event_type", columnList = "event_type"),
    @Index(name = "idx_event_timestamp", columnList = "event_timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class MobilityEvent extends BaseEntity {
    
    @NotNull(message = "Vehicle is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;
    
    @NotBlank(message = "Event description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Event timestamp is required")
    @Column(nullable = false)
    private LocalDateTime eventTimestamp;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(nullable = false)
    private Double speed = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventSeverity severity = EventSeverity.INFO;
    
    @Column(columnDefinition = "JSON")
    private String metadata;
    
    @Column(nullable = false)
    private Boolean processed = false;
    
    public enum EventType {
        VEHICLE_START, VEHICLE_STOP, SPEED_CHANGE, LOCATION_UPDATE,
        TRAFFIC_CONGESTION, ACCIDENT_DETECTED, FUEL_LOW, 
        MAINTENANCE_ALERT, SYSTEM_ERROR
    }
    
    public enum EventSeverity {
        INFO, WARNING, CRITICAL
    }
}
