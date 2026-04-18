package com.urban.urban_move.service;

import com.urban.urban_move.domain.Vehicle;
import com.urban.urban_move.dto.CreateVehicleRequest;
import com.urban.urban_move.dto.VehicleDTO;
import com.urban.urban_move.exception.ResourceNotFoundException;
import com.urban.urban_move.repository.VehicleRepository;
import com.urban.urban_move.repository.UserRepository;
import com.urban.urban_move.repository.FleetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Vehicle service for managing vehicles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final FleetRepository fleetRepository;
    
    /**
     * Create a new vehicle.
     */
    @Transactional
    public VehicleDTO createVehicle(CreateVehicleRequest request) {
        log.info("Creating new vehicle with plate number: {}", request.getPlateNumber());
        
        Vehicle vehicle = Vehicle.builder()
            .plateNumber(request.getPlateNumber())
            .vehicleType(request.getVehicleType())
            .make(request.getMake())
            .model(request.getModel())
            .year(request.getYear())
            .capacity(request.getCapacity())
            .currentLatitude(request.getCurrentLatitude())
            .currentLongitude(request.getCurrentLongitude())
            .status(Vehicle.VehicleStatus.IDLE)
            .lastLocationUpdateTime(System.currentTimeMillis())
            .build();
        
        if (request.getDriverId() != null) {
            var driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver", "id", request.getDriverId()));
            vehicle.setDriver(driver);
        }
        
        if (request.getFleetId() != null) {
            var fleet = fleetRepository.findById(request.getFleetId())
                .orElseThrow(() -> new ResourceNotFoundException("Fleet", "id", request.getFleetId()));
            vehicle.setFleet(fleet);
        }
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle created successfully with ID: {}", savedVehicle.getId());
        
        return convertToDTO(savedVehicle);
    }
    
    /**
     * Get vehicle by ID.
     */
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        log.info("Fetching vehicle with ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
        return convertToDTO(vehicle);
    }
    
    /**
     * Get vehicle by plate number.
     */
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleByPlateNumber(String plateNumber) {
        log.info("Fetching vehicle with plate number: {}", plateNumber);
        Vehicle vehicle = vehicleRepository.findByPlateNumber(plateNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "plateNumber", plateNumber));
        return convertToDTO(vehicle);
    }
    
    /**
     * Get all vehicles with pagination.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDTO> getAllVehicles(Pageable pageable) {
        log.info("Fetching all vehicles with pagination");
        return vehicleRepository.findAll(pageable)
            .map(this::convertToDTO);
    }
    
    /**
     * Get vehicles by status.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDTO> getVehiclesByStatus(Vehicle.VehicleStatus status, Pageable pageable) {
        log.info("Fetching vehicles with status: {}", status);
        return vehicleRepository.findByStatus(status, pageable)
            .map(this::convertToDTO);
    }
    
    /**
     * Get vehicles by fleet.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDTO> getVehiclesByFleet(Long fleetId, Pageable pageable) {
        log.info("Fetching vehicles for fleet ID: {}", fleetId);
        return vehicleRepository.findByFleetId(fleetId, pageable)
            .map(this::convertToDTO);
    }
    
    /**
     * Update vehicle location.
     */
    @Transactional
    public VehicleDTO updateVehicleLocation(Long id, Double latitude, Double longitude, Double speed) {
        log.info("Updating location for vehicle ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
        
        vehicle.setCurrentLatitude(latitude);
        vehicle.setCurrentLongitude(longitude);
        vehicle.setCurrentSpeed(speed);
        vehicle.setLastLocationUpdateTime(System.currentTimeMillis());
        
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle location updated successfully");
        
        return convertToDTO(updatedVehicle);
    }
    
    /**
     * Update vehicle status.
     */
    @Transactional
    public VehicleDTO updateVehicleStatus(Long id, Vehicle.VehicleStatus status) {
        log.info("Updating status for vehicle ID: {} to: {}", id, status);
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
        
        vehicle.setStatus(status);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle status updated successfully");
        
        return convertToDTO(updatedVehicle);
    }
    
    /**
     * Update vehicle fuel level.
     */
    @Transactional
    public void updateFuelLevel(Long id, Double fuelLevel) {
        log.info("Updating fuel level for vehicle ID: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
        
        vehicle.setFuelLevel(fuelLevel);
        vehicleRepository.save(vehicle);
    }
    
    /**
     * Convert Vehicle entity to VehicleDTO.
     */
    public VehicleDTO convertToDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
            .id(vehicle.getId())
            .plateNumber(vehicle.getPlateNumber())
            .vehicleType(vehicle.getVehicleType())
            .make(vehicle.getMake())
            .model(vehicle.getModel())
            .year(vehicle.getYear())
            .capacity(vehicle.getCapacity())
            .currentLatitude(vehicle.getCurrentLatitude())
            .currentLongitude(vehicle.getCurrentLongitude())
            .currentSpeed(vehicle.getCurrentSpeed())
            .status(vehicle.getStatus().name())
            .fuelLevel(vehicle.getFuelLevel())
            .totalDistance(vehicle.getTotalDistance())
            .driverName(vehicle.getDriver() != null ? vehicle.getDriver().getFullName() : null)
            .fleetCode(vehicle.getFleet() != null ? vehicle.getFleet().getFleetCode() : null)
            .createdAt(vehicle.getCreatedAt())
            .updatedAt(vehicle.getUpdatedAt())
            .build();
    }

    /**
     * Get all vehicles with pagination (returns Page of Vehicle entity).
     */
    @Transactional(readOnly = true)
    public Page<Vehicle> getAllVehiclesPage(Pageable pageable) {
        log.info("Fetching all vehicles with pagination");
        return vehicleRepository.findAll(pageable);
    }

    /**
     * Get all vehicles as list.
     */
    @Transactional(readOnly = true)
    public java.util.List<Vehicle> getAllVehiclesList() {
        log.info("Fetching all vehicles as list");
        return vehicleRepository.findAll();
    }

    /**
     * Get vehicle entity by ID (not DTO).
     */
    @Transactional(readOnly = true)
    public Vehicle getVehicleEntityById(Long id) {
        log.info("Fetching vehicle entity with ID: {}", id);
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", id));
    }

    /**
     * Create vehicle from form data.
     */
    @Transactional
    public Vehicle createVehicleFromForm(Vehicle vehicle) {
        log.info("Creating vehicle from form: {}", vehicle.getPlateNumber());
        return vehicleRepository.save(vehicle);
    }

    /**
     * Update vehicle from form data.
     */
    @Transactional
    public Vehicle updateVehicleFromForm(Vehicle vehicle) {
        log.info("Updating vehicle from form: {}", vehicle.getPlateNumber());
        return vehicleRepository.save(vehicle);
    }

    /**
     * Delete vehicle by ID.
     */
    @Transactional
    public void deleteVehicle(Long id) {
        log.info("Deleting vehicle with ID: {}", id);
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle", "id", id);
        }
        vehicleRepository.deleteById(id);
    }
}