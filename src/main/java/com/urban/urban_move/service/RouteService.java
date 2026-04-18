package com.urban.urban_move.service;

import com.urban.urban_move.domain.Route;
import com.urban.urban_move.dto.CreateRouteRequest;
import com.urban.urban_move.dto.RouteDTO;
import com.urban.urban_move.exception.ResourceNotFoundException;
import com.urban.urban_move.repository.RouteRepository;
import com.urban.urban_move.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Route service for managing routes and route optimization.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {
    
    private final RouteRepository routeRepository;
    private final VehicleRepository vehicleRepository;
    
    /**
     * Create a new route.
     */
    @Transactional
    public RouteDTO createRoute(CreateRouteRequest request) {
        log.info("Creating new route: {}", request.getName());
        
        Route route = Route.builder()
            .name(request.getName())
            .startLocation(request.getStartLocation())
            .endLocation(request.getEndLocation())
            .startLatitude(request.getStartLatitude())
            .startLongitude(request.getStartLongitude())
            .endLatitude(request.getEndLatitude())
            .endLongitude(request.getEndLongitude())
            .distanceKm(request.getDistanceKm())
            .estimatedDurationMinutes(request.getEstimatedDurationMinutes())
            .status(Route.RouteStatus.PLANNED)
            .build();
        
        if (request.getVehicleId() != null) {
            var vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", request.getVehicleId()));
            route.setVehicle(vehicle);
        }
        
        Route savedRoute = routeRepository.save(route);
        log.info("Route created successfully with ID: {}", savedRoute.getId());
        
        return convertToDTO(savedRoute);
    }
    
    /**
     * Get route by ID.
     */
    @Transactional(readOnly = true)
    public RouteDTO getRouteById(Long id) {
        log.info("Fetching route with ID: {}", id);
        Route route = routeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));
        return convertToDTO(route);
    }
    
    /**
     * Get all routes with pagination.
     */
    @Transactional(readOnly = true)
    public Page<RouteDTO> getAllRoutes(Pageable pageable) {
        log.info("Fetching all routes with pagination");
        return routeRepository.findAll(pageable)
            .map(this::convertToDTO);
    }
    
    /**
     * Get routes by status.
     */
    @Transactional(readOnly = true)
    public Page<RouteDTO> getRoutesByStatus(Route.RouteStatus status, Pageable pageable) {
        log.info("Fetching routes with status: {}", status);
        return routeRepository.findByStatus(status, pageable)
            .map(this::convertToDTO);
    }
    
    /**
     * Get routes by vehicle.
     */
    @Transactional(readOnly = true)
    public Page<RouteDTO> getRoutesByVehicle(Long vehicleId, Pageable pageable) {
        log.info("Fetching routes for vehicle ID: {}", vehicleId);
        return routeRepository.findByVehicleId(vehicleId, pageable)
            .map(this::convertToDTO);
    }
    
    /**
     * Start route.
     */
    @Transactional
    public RouteDTO startRoute(Long id) {
        log.info("Starting route with ID: {}", id);
        Route route = routeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));
        
        route.setStatus(Route.RouteStatus.IN_PROGRESS);
        Route updatedRoute = routeRepository.save(route);
        log.info("Route started successfully");
        
        return convertToDTO(updatedRoute);
    }
    
    /**
     * Complete route.
     */
    @Transactional
    public RouteDTO completeRoute(Long id, Integer actualDurationMinutes, Double actualDistanceKm) {
        log.info("Completing route with ID: {}", id);
        Route route = routeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));
        
        route.setStatus(Route.RouteStatus.COMPLETED);
        route.setActualDurationMinutes(actualDurationMinutes);
        route.setActualDistanceKm(actualDistanceKm);
        
        Route updatedRoute = routeRepository.save(route);
        log.info("Route completed successfully");
        
        return convertToDTO(updatedRoute);
    }
    
    /**
     * Convert Route entity to RouteDTO.
     */
    public RouteDTO convertToDTO(Route route) {
        return RouteDTO.builder()
            .id(route.getId())
            .name(route.getName())
            .startLocation(route.getStartLocation())
            .endLocation(route.getEndLocation())
            .startLatitude(route.getStartLatitude())
            .startLongitude(route.getStartLongitude())
            .endLatitude(route.getEndLatitude())
            .endLongitude(route.getEndLongitude())
            .distanceKm(route.getDistanceKm())
            .estimatedDurationMinutes(route.getEstimatedDurationMinutes())
            .actualDurationMinutes(route.getActualDurationMinutes())
            .actualDistanceKm(route.getActualDistanceKm())
            .status(route.getStatus().name())
            .vehiclePlateNumber(route.getVehicle() != null ? route.getVehicle().getPlateNumber() : null)
            .createdAt(route.getCreatedAt())
            .updatedAt(route.getUpdatedAt())
            .build();
    }

    /**
     * Get all routes with pagination (returns Page of Route entity).
     */
    @Transactional(readOnly = true)
    public Page<Route> getAllRoutesPage(Pageable pageable) {
        log.info("Fetching all routes with pagination");
        return routeRepository.findAll(pageable);
    }

    /**
     * Get all routes as list.
     */
    @Transactional(readOnly = true)
    public java.util.List<Route> getAllRoutesList() {
        log.info("Fetching all routes as list");
        return routeRepository.findAll();
    }

    /**
     * Create route from form data.
     */
    @Transactional
    public Route createRouteFromForm(Route route) {
        log.info("Creating route from form: {}", route.getName());
        if (route.getStatus() == null) {
            route.setStatus(Route.RouteStatus.PLANNED);
        }
        return routeRepository.save(route);
    }

    /**
     * Update route from form data.
     */
    @Transactional
    public Route updateRouteFromForm(Route route) {
        log.info("Updating route from form: {}", route.getName());
        return routeRepository.save(route);
    }

    /**
     * Delete route by ID.
     */
    @Transactional
    public void deleteRoute(Long id) {
        log.info("Deleting route with ID: {}", id);
        if (!routeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Route", "id", id);
        }
        routeRepository.deleteById(id);
    }
}