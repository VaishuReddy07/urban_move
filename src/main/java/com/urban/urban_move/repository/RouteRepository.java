package com.urban.urban_move.repository;

import com.urban.urban_move.domain.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Route entity.
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    Page<Route> findByStatus(Route.RouteStatus status, Pageable pageable);
    
    Page<Route> findByVehicleId(Long vehicleId, Pageable pageable);
    
    List<Route> findByVehicleIdAndStatus(Long vehicleId, Route.RouteStatus status);
}
