package com.urban.urban_move.repository;

import com.urban.urban_move.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Notification entity.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    
    Page<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead, Pageable pageable);
    
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    
    Page<Notification> findByUserIdAndIsReadFalse(Long userId, Pageable pageable);
    
    long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
