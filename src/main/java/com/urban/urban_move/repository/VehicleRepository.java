package com.urban.urban_move.repository;

import com.urban.urban_move.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Vehicle entity.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findByPlateNumber(String plateNumber);
    
    Page<Vehicle> findByStatus(Vehicle.VehicleStatus status, Pageable pageable);
    
    Page<Vehicle> findByFleetId(Long fleetId, Pageable pageable);
    
    Page<Vehicle> findByDriverId(Long driverId, Pageable pageable);
    
    List<Vehicle> findByFleetIdAndStatus(Long fleetId, Vehicle.VehicleStatus status);
    
    @Query("SELECT v FROM Vehicle v WHERE v.fuelLevel < :threshold AND v.status = 'IN_SERVICE'")
    List<Vehicle> findVehiclesWithLowFuel(@Param("threshold") Double threshold);
}
