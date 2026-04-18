package com.urban.urban_move.controller;

import com.urban.urban_move.domain.Vehicle;
import com.urban.urban_move.dto.ApiResponse;
import com.urban.urban_move.dto.CreateVehicleRequest;
import com.urban.urban_move.dto.VehicleDTO;
import com.urban.urban_move.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Vehicle management REST API controller.
 */
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Vehicles", description = "Vehicle management and tracking endpoints")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class VehicleController {
    
    private final VehicleService vehicleService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Create new vehicle", description = "Register a new vehicle in the system")
    public ResponseEntity<ApiResponse<VehicleDTO>> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        log.info("Create vehicle endpoint called with plate: {}", request.getPlateNumber());
        VehicleDTO vehicleDTO = vehicleService.createVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(vehicleDTO, "Vehicle created successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER', 'RIDER')")
    @Operation(summary = "Get vehicle by ID", description = "Retrieve vehicle information by ID")
    public ResponseEntity<ApiResponse<VehicleDTO>> getVehicleById(@PathVariable Long id) {
        log.info("Get vehicle endpoint called with ID: {}", id);
        VehicleDTO vehicleDTO = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.success(vehicleDTO));
    }
    
    @GetMapping("/plate/{plateNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER', 'RIDER')")
    @Operation(summary = "Get vehicle by plate number", description = "Retrieve vehicle information by plate number")
    public ResponseEntity<ApiResponse<VehicleDTO>> getVehicleByPlateNumber(@PathVariable String plateNumber) {
        log.info("Get vehicle by plate endpoint called with: {}", plateNumber);
        VehicleDTO vehicleDTO = vehicleService.getVehicleByPlateNumber(plateNumber);
        return ResponseEntity.ok(ApiResponse.success(vehicleDTO));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER', 'RIDER')")
    @Operation(summary = "Get all vehicles", description = "Retrieve all vehicles with pagination and filtering")
    public ResponseEntity<ApiResponse<Page<VehicleDTO>>> getAllVehicles(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long fleetId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        log.info("Get all vehicles endpoint called");
        Pageable pageable = PageRequest.of(page, size);
        Page<VehicleDTO> vehicles;
        
        if (status != null) {
            vehicles = vehicleService.getVehiclesByStatus(Vehicle.VehicleStatus.valueOf(status), pageable);
        } else if (fleetId != null) {
            vehicles = vehicleService.getVehiclesByFleet(fleetId, pageable);
        } else {
            vehicles = vehicleService.getAllVehicles(pageable);
        }
        
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }
    
    @PutMapping("/{id}/location")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER')")
    @Operation(summary = "Update vehicle location", description = "Update vehicle GPS location and speed")
    public ResponseEntity<ApiResponse<VehicleDTO>> updateVehicleLocation(
        @PathVariable Long id,
        @RequestParam Double latitude,
        @RequestParam Double longitude,
        @RequestParam(required = false, defaultValue = "0") Double speed) {
        log.info("Update vehicle location endpoint called for ID: {}", id);
        VehicleDTO vehicleDTO = vehicleService.updateVehicleLocation(id, latitude, longitude, speed);
        return ResponseEntity.ok(ApiResponse.success(vehicleDTO, "Vehicle location updated"));
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER')")
    @Operation(summary = "Update vehicle status", description = "Change vehicle operational status")
    public ResponseEntity<ApiResponse<VehicleDTO>> updateVehicleStatus(
        @PathVariable Long id,
        @RequestParam String status) {
        log.info("Update vehicle status endpoint called for ID: {}", id);
        VehicleDTO vehicleDTO = vehicleService.updateVehicleStatus(id, Vehicle.VehicleStatus.valueOf(status));
        return ResponseEntity.ok(ApiResponse.success(vehicleDTO, "Vehicle status updated"));
    }
    
    @PutMapping("/{id}/fuel")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER')")
    @Operation(summary = "Update fuel level", description = "Update vehicle fuel level percentage")
    public ResponseEntity<ApiResponse<String>> updateFuelLevel(
        @PathVariable Long id,
        @RequestParam Double fuelLevel) {
        log.info("Update fuel level endpoint called for ID: {}", id);
        vehicleService.updateFuelLevel(id, fuelLevel);
        return ResponseEntity.ok(ApiResponse.success("Fuel level updated successfully"));
    }
}
