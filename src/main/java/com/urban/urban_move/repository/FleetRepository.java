package com.urban.urban_move.repository;

import com.urban.urban_move.domain.Fleet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Fleet entity.
 */
@Repository
public interface FleetRepository extends JpaRepository<Fleet, Long> {
    
    Optional<Fleet> findByFleetCode(String fleetCode);
    
    Page<Fleet> findByStatus(Fleet.FleetStatus status, Pageable pageable);
    
    Page<Fleet> findByFleetManagerId(Long managerId, Pageable pageable);
    
    boolean existsByFleetCode(String fleetCode);
}
