package com.urban.urban_move.controller;

import com.urban.urban_move.domain.Route;
import com.urban.urban_move.dto.ApiResponse;
import com.urban.urban_move.dto.CreateRouteRequest;
import com.urban.urban_move.dto.RouteDTO;
import com.urban.urban_move.service.RouteService;
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
 * Route management REST API controller.
 */
@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Routes", description = "Route management and optimization endpoints")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class RouteController {
    
    private final RouteService routeService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @Operation(summary = "Create new route", description = "Plan a new route for vehicle")
    public ResponseEntity<ApiResponse<RouteDTO>> createRoute(@Valid @RequestBody CreateRouteRequest request) {
        log.info("Create route endpoint called with name: {}", request.getName());
        RouteDTO routeDTO = routeService.createRoute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(routeDTO, "Route created successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER', 'RIDER')")
    @Operation(summary = "Get route by ID", description = "Retrieve route details by ID")
    public ResponseEntity<ApiResponse<RouteDTO>> getRouteById(@PathVariable Long id) {
        log.info("Get route endpoint called with ID: {}", id);
        RouteDTO routeDTO = routeService.getRouteById(id);
        return ResponseEntity.ok(ApiResponse.success(routeDTO));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER', 'RIDER')")
    @Operation(summary = "Get all routes", description = "Retrieve all routes with pagination and filtering")
    public ResponseEntity<ApiResponse<Page<RouteDTO>>> getAllRoutes(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long vehicleId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        log.info("Get all routes endpoint called");
        Pageable pageable = PageRequest.of(page, size);
        Page<RouteDTO> routes;
        
        if (status != null) {
            routes = routeService.getRoutesByStatus(Route.RouteStatus.valueOf(status), pageable);
        } else if (vehicleId != null) {
            routes = routeService.getRoutesByVehicle(vehicleId, pageable);
        } else {
            routes = routeService.getAllRoutes(pageable);
        }
        
        return ResponseEntity.ok(ApiResponse.success(routes));
    }
    
    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER')")
    @Operation(summary = "Start route", description = "Mark route as started")
    public ResponseEntity<ApiResponse<RouteDTO>> startRoute(@PathVariable Long id) {
        log.info("Start route endpoint called for ID: {}", id);
        RouteDTO routeDTO = routeService.startRoute(id);
        return ResponseEntity.ok(ApiResponse.success(routeDTO, "Route started successfully"));
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR', 'DRIVER')")
    @Operation(summary = "Complete route", description = "Mark route as completed with actual metrics")
    public ResponseEntity<ApiResponse<RouteDTO>> completeRoute(
        @PathVariable Long id,
        @RequestParam Integer actualDurationMinutes,
        @RequestParam Double actualDistanceKm) {
        log.info("Complete route endpoint called for ID: {}", id);
        RouteDTO routeDTO = routeService.completeRoute(id, actualDurationMinutes, actualDistanceKm);
        return ResponseEntity.ok(ApiResponse.success(routeDTO, "Route completed successfully"));
    }
}
