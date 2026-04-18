package com.urban.urban_move.service;

import com.urban.urban_move.domain.Fleet;
import com.urban.urban_move.repository.FleetRepository;
import com.urban.urban_move.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FleetService {
    
    @Autowired
    private FleetRepository fleetRepository;
    
    public Page<Fleet> getAllFleets(Pageable pageable) {
        return fleetRepository.findAll(pageable);
    }
    
    public List<Fleet> getAllFleets() {
        return fleetRepository.findAll();
    }
    
    public List<Fleet> getAllFleetsList() {
        return fleetRepository.findAll();
    }
    
    public Fleet getFleetById(Long id) {
        return fleetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fleet not found with id: " + id));
    }
    
    public Fleet createFleet(Fleet fleet) {
        return fleetRepository.save(fleet);
    }
    
    public Fleet updateFleet(Fleet fleet) {
        if (!fleetRepository.existsById(fleet.getId())) {
            throw new ResourceNotFoundException("Fleet not found with id: " + fleet.getId());
        }
        return fleetRepository.save(fleet);
    }
    
    public void deleteFleet(Long id) {
        if (!fleetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fleet not found with id: " + id);
        }
        fleetRepository.deleteById(id);
    }
    
    public Fleet getFleetByCode(String fleetCode) {
        return fleetRepository.findByFleetCode(fleetCode)
            .orElseThrow(() -> new ResourceNotFoundException("Fleet not found with code: " + fleetCode));
    }
}
