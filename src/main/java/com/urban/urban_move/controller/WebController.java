package com.urban.urban_move.controller;

import com.urban.urban_move.security.CustomUserDetails;
import com.urban.urban_move.service.VehicleService;
import com.urban.urban_move.service.RouteService;
import com.urban.urban_move.repository.VehicleRepository;
import com.urban.urban_move.repository.RouteRepository;
import com.urban.urban_move.repository.UserRepository;
import com.urban.urban_move.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MVC Controller for web pages with data fetching.
 */
@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final VehicleService vehicleService;
    private final RouteService routeService;
    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            // Get current user details
            User user = userRepository.findById(userDetails.getId()).orElse(null);
            model.addAttribute("user", user);
            
            // Dashboard statistics
            Pageable pageable = PageRequest.of(0, 10);
            long totalVehicles = vehicleRepository.count();
            long totalRoutes = routeRepository.count();
            long activeVehicles = vehicleRepository.findAll().stream()
                .filter(v -> v.getStatus() != null && v.getStatus().toString().equals("ACTIVE"))
                .count();
            
            model.addAttribute("totalVehicles", totalVehicles);
            model.addAttribute("totalRoutes", totalRoutes);
            model.addAttribute("activeVehicles", activeVehicles);
            model.addAttribute("completedRoutes", totalRoutes - activeVehicles);
            model.addAttribute("systemHealth", "99.9%");
            
            // Recent vehicles
            model.addAttribute("recentVehicles", vehicleService.getAllVehicles(pageable).getContent());
            
            // Recent routes
            model.addAttribute("recentRoutes", routeService.getAllRoutes(pageable).getContent());
        }
        return "dashboard";
    }
    
    @GetMapping("/analytics")
    public String analytics(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            // Analytics data
            long totalVehicles = vehicleRepository.count();
            long totalRoutes = routeRepository.count();
            long totalUsers = userRepository.count();
            long activeVehicles = vehicleRepository.findAll().stream()
                .filter(v -> v.getStatus() != null && v.getStatus().toString().equals("ACTIVE"))
                .count();
            
            model.addAttribute("totalVehicles", totalVehicles);
            model.addAttribute("totalRoutes", totalRoutes);
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("activeVehicles", activeVehicles);
            model.addAttribute("totalDistance", "0 km");
            model.addAttribute("completionRate", "98%");
            model.addAttribute("routeGrowth", "12%");
            model.addAttribute("vehicleGrowth", "8%");
            model.addAttribute("distanceGrowth", "5%");
        }
        return "analytics";
    }
    
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userRepository.findById(userDetails.getId()).orElse(null);
            model.addAttribute("user", user);
            
            // Add user statistics
            if (user != null) {
                // User role/membership type
                String membershipType = user.getRole() != null ? user.getRole().name() : "USER";
                model.addAttribute("membershipType", membershipType);
                
                // Account creation date
                model.addAttribute("createdAt", user.getCreatedAt());
                
                // User statistics (can be expanded with actual data from service)
                model.addAttribute("totalTrips", 0);
                model.addAttribute("totalDistance", "0 km");
                model.addAttribute("accountAge", "New Account");
            }
        }
        return "profile";
    }
    
    @GetMapping("/settings")
    public String settings(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userRepository.findById(userDetails.getId()).orElse(null);
            model.addAttribute("user", user);
        }
        return "settings";
    }
}
