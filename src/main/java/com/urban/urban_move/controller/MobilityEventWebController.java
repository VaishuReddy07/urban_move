package com.urban.urban_move.controller;

import com.urban.urban_move.domain.MobilityEvent;
import com.urban.urban_move.service.MobilityEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class MobilityEventWebController {
    
    @Autowired
    private MobilityEventService mobilityEventService;
    
    @GetMapping
    public String listEvents(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<MobilityEvent> events = mobilityEventService.getAllEvents(PageRequest.of(page, 15));
        model.addAttribute("events", events.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", events.getTotalPages());
        model.addAttribute("totalEvents", events.getTotalElements());
        return "events";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("event", new MobilityEvent());
        return "event-form";
    }
    
    @PostMapping("/add")
    public String addEvent(@ModelAttribute MobilityEvent event) {
        mobilityEventService.createEvent(event);
        return "redirect:/events";
    }
    
    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        MobilityEvent event = mobilityEventService.getEventById(id);
        model.addAttribute("event", event);
        return "event-detail";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        mobilityEventService.deleteEvent(id);
        return "redirect:/events";
    }
}
