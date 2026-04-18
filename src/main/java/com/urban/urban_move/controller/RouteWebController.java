package com.urban.urban_move.controller;

import com.urban.urban_move.domain.Route;
import com.urban.urban_move.domain.Vehicle;
import com.urban.urban_move.dto.RouteDTO;
import com.urban.urban_move.service.RouteService;
import com.urban.urban_move.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteWebController {
    
    @Autowired
    private RouteService routeService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping
    public String listRoutes(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Route> routes = routeService.getAllRoutesPage(PageRequest.of(page, 10));
        
        model.addAttribute("routes", routes.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", routes.getTotalPages());
        model.addAttribute("totalRoutes", routes.getTotalElements());
        model.addAttribute("activeRoutes", routes.getContent().stream()
            .filter(r -> r.getStatus() == Route.RouteStatus.IN_PROGRESS)
            .count());
        
        return "routes";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("route", new Route());
        model.addAttribute("vehicles", vehicleService.getAllVehiclesList());
        model.addAttribute("statuses", Route.RouteStatus.values());
        return "route-form";
    }
    
    @PostMapping("/add")
    public String addRoute(@ModelAttribute Route route) {
        routeService.createRouteFromForm(route);
        return "redirect:/routes";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        RouteDTO route = routeService.getRouteById(id);
        model.addAttribute("route", route);
        model.addAttribute("vehicles", vehicleService.getAllVehiclesList());
        model.addAttribute("statuses", Route.RouteStatus.values());
        return "route-form";
    }
    
    @PostMapping("/edit/{id}")
    public String updateRoute(@PathVariable Long id, @ModelAttribute Route route) {
        route.setId(id);
        routeService.updateRouteFromForm(route);
        return "redirect:/routes";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return "redirect:/routes";
    }
    
    @GetMapping("/{id}")
    public String viewRoute(@PathVariable Long id, Model model) {
        RouteDTO route = routeService.getRouteById(id);
        model.addAttribute("route", route);
        return "route-detail";
    }
}
