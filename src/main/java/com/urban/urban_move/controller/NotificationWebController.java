package com.urban.urban_move.controller;

import com.urban.urban_move.domain.Notification;
import com.urban.urban_move.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.urban.urban_move.security.CustomUserDetails;

@Controller
@RequestMapping("/notifications")
public class NotificationWebController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public String listNotifications(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @RequestParam(defaultValue = "0") int page, Model model) {
        Page<Notification> notifications = notificationService.getUserNotifications(userDetails.getId(), PageRequest.of(page, 20));
        model.addAttribute("notifications", notifications.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notifications.getTotalPages());
        model.addAttribute("totalNotifications", notifications.getTotalElements());
        long unreadCount = notifications.getContent().stream().filter(n -> !n.getIsRead()).count();
        model.addAttribute("unreadCount", unreadCount);
        return "notifications";
    }
    
    @PostMapping("/{id}/mark-read")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications";
    }
    
    @PostMapping("/mark-all-read")
    public String markAllAsRead(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getId());
        return "redirect:/notifications";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }
}
