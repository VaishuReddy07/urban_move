package com.urban.urban_move.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Notification entity for system alerts and user notifications.
 */
@Entity
@Table(name = "notifications", indexes = @Index(name = "idx_user_read", columnList = "user_id,is_read"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Notification extends BaseEntity {
    
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false, length = 100)
    private String title;
    
    @NotBlank(message = "Message is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationPriority priority = NotificationPriority.NORMAL;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    @Column(length = 500)
    private String actionUrl;
    
    public enum NotificationType {
        SYSTEM, ALERT, REMINDER, ACHIEVEMENT, UPDATE
    }
    
    public enum NotificationPriority {
        LOW, NORMAL, HIGH, CRITICAL
    }
}
