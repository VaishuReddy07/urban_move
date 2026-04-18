package com.urban.urban_move.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Permission entity for fine-grained access control.
 */
@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Permission extends BaseEntity {
    
    @NotBlank(message = "Permission code is required")
    @Column(nullable = false, unique = true, length = 100)
    private String code;
    
    @NotBlank(message = "Permission name is required")
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionCategory category;
    
    public enum PermissionCategory {
        USER_MANAGEMENT, VEHICLE_MANAGEMENT, ROUTE_MANAGEMENT, 
        FLEET_MANAGEMENT, ANALYTICS, SYSTEM_ADMIN
    }
}
