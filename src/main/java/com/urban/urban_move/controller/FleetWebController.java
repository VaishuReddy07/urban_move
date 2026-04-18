package com.urban.urban_move.controller;

import com.urban.urban_move.domain.Fleet;
import com.urban.urban_move.service.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fleets")
public class FleetWebController {
    
    @Autowired
    private FleetService fleetService;
    
    @GetMapping
    public String listFleets(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Fleet> fleets = fleetService.getAllFleets(PageRequest.of(page, 10));
        model.addAttribute("fleets", fleets.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", fleets.getTotalPages());
        model.addAttribute("totalFleets", fleets.getTotalElements());
        return "fleets";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("fleet", new Fleet());
        return "fleet-form";
    }
    
    @PostMapping("/add")
    public String addFleet(@ModelAttribute Fleet fleet) {
        fleetService.createFleet(fleet);
        return "redirect:/fleets";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Fleet fleet = fleetService.getFleetById(id);
        model.addAttribute("fleet", fleet);
        return "fleet-form";
    }
    
    @PostMapping("/edit/{id}")
    public String updateFleet(@PathVariable Long id, @ModelAttribute Fleet fleet) {
        fleet.setId(id);
        fleetService.updateFleet(fleet);
        return "redirect:/fleets";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteFleet(@PathVariable Long id) {
        fleetService.deleteFleet(id);
        return "redirect:/fleets";
    }
}
