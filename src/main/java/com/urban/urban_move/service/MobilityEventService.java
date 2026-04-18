package com.urban.urban_move.service;

import com.urban.urban_move.domain.MobilityEvent;
import com.urban.urban_move.repository.MobilityEventRepository;
import com.urban.urban_move.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MobilityEventService {
    
    @Autowired
    private MobilityEventRepository mobilityEventRepository;
    
    public Page<MobilityEvent> getAllEvents(Pageable pageable) {
        return mobilityEventRepository.findAll(pageable);
    }
    
    public List<MobilityEvent> getAllEvents() {
        return mobilityEventRepository.findAll();
    }
    
    public MobilityEvent getEventById(Long id) {
        return mobilityEventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }
    
    public MobilityEvent createEvent(MobilityEvent event) {
        return mobilityEventRepository.save(event);
    }
    
    public MobilityEvent updateEvent(MobilityEvent event) {
        if (!mobilityEventRepository.existsById(event.getId())) {
            throw new ResourceNotFoundException("Event not found with id: " + event.getId());
        }
        return mobilityEventRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        if (!mobilityEventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        mobilityEventRepository.deleteById(id);
    }
    
    public Page<MobilityEvent> getEventsByVehicleId(Long vehicleId, Pageable pageable) {
        return mobilityEventRepository.findByVehicleId(vehicleId, pageable);
    }
    
    public Page<MobilityEvent> getEventsBySeverity(String severity, Pageable pageable) {
        return mobilityEventRepository.findBySeverity(MobilityEvent.EventSeverity.valueOf(severity), pageable);
    }
}
