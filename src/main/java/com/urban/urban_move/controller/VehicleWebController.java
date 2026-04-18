package com.urban.urban_move.controller;

import com.urban.urban_move.domain.Fleet;
import com.urban.urban_move.domain.Vehicle;
import com.urban.urban_move.dto.CreateVehicleRequest;
import com.urban.urban_move.service.VehicleService;
import com.urban.urban_move.service.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vehicles")
public class VehicleWebController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private FleetService fleetService;
    
    @GetMapping
    public String listVehicles(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Vehicle> vehicles = vehicleService.getAllVehiclesPage(PageRequest.of(page, 10));
        List<Fleet> fleets = fleetService.getAllFleetsList();
        
        model.addAttribute("vehicles", vehicles.getContent());
        model.addAttribute("fleets", fleets);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vehicles.getTotalPages());
        model.addAttribute("totalVehicles", vehicles.getTotalElements());
        model.addAttribute("activeVehicles", vehicles.getContent().stream()
            .filter(v -> v.getStatus() == Vehicle.VehicleStatus.ACTIVE)
            .count());
        
        return "vehicles";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("fleets", fleetService.getAllFleetsList());
        model.addAttribute("statuses", Vehicle.VehicleStatus.values());
        return "vehicle-form";
    }
    
    @PostMapping("/add")
    public String addVehicle(@ModelAttribute Vehicle vehicle) {
        vehicleService.createVehicleFromForm(vehicle);
        return "redirect:/vehicles";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.getVehicleEntityById(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("fleets", fleetService.getAllFleetsList());
        model.addAttribute("statuses", Vehicle.VehicleStatus.values());
        return "vehicle-form";
    }
    
    @PostMapping("/edit/{id}")
    public String updateVehicle(@PathVariable Long id, @ModelAttribute Vehicle vehicle) {
        vehicle.setId(id);
        vehicleService.updateVehicleFromForm(vehicle);
        return "redirect:/vehicles";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicles";
    }
    
    @GetMapping("/{id}")
    public String viewVehicle(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.getVehicleEntityById(id);
        model.addAttribute("vehicle", vehicle);
        return "vehicle-detail";
    }
}
