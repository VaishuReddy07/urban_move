package com.urban.urban_move.repository;

import com.urban.urban_move.domain.MobilityEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for MobilityEvent entity.
 */
@Repository
public interface MobilityEventRepository extends JpaRepository<MobilityEvent, Long> {
    
    Page<MobilityEvent> findByVehicleId(Long vehicleId, Pageable pageable);
    
    Page<MobilityEvent> findByEventType(MobilityEvent.EventType eventType, Pageable pageable);
    
    @Query("SELECT e FROM MobilityEvent e WHERE e.vehicle.id = :vehicleId AND e.eventTimestamp BETWEEN :startTime AND :endTime")
    List<MobilityEvent> findEventsForVehicleInTimeRange(
        @Param("vehicleId") Long vehicleId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    List<MobilityEvent> findBySeverity(MobilityEvent.EventSeverity severity);
    
    Page<MobilityEvent> findBySeverity(MobilityEvent.EventSeverity severity, Pageable pageable);
    
    @Query("SELECT e FROM MobilityEvent e WHERE e.severity = :severity AND e.processed = false ORDER BY e.eventTimestamp DESC")
    Page<MobilityEvent> findUnprocessedCriticalEvents(
        @Param("severity") MobilityEvent.EventSeverity severity,
        Pageable pageable
    );
}
