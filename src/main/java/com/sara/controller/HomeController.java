package com.sara.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sara.entity.Request;
import com.sara.entity.Resource;
import com.sara.entity.ResourceAssignment;
import com.sara.service.RequestService;
import com.sara.service.ResourceAssignmentService;
import com.sara.service.ResourceService;
import com.sara.service.UserService;

@Controller
public class HomeController {
    
    private final ResourceService resourceService;
    private final RequestService requestService;
    private final UserService userService;
    private final ResourceAssignmentService resourceAssignmentService;
    
    public HomeController(ResourceService resourceService,
                          RequestService requestService,
                          UserService userService,
                          ResourceAssignmentService resourceAssignmentService) {
        this.resourceService = resourceService;
        this.requestService = requestService;
        this.userService = userService;
        this.resourceAssignmentService = resourceAssignmentService;
    }
    
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Get recent resources and requests for the home page
        List<Resource> recentResources = resourceService.findAvailableResources().stream()
                .limit(6)
                .toList();
        List<Request> urgentRequests = requestService.findOpenRequests().stream()
                .limit(6)
                .toList();
        
        model.addAttribute("recentResources", recentResources);
        model.addAttribute("urgentRequests", urgentRequests);
        
        return "home";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        var user = userService.findByUsername(principal.getName());
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user.get());
        
        // Redirect to role-specific dashboards handled by their controllers (ensures all required model attributes are set)
        return switch (user.get().getRole()) {
            case ADMIN -> "redirect:/admin/dashboard";
            case DONOR -> "redirect:/donor/dashboard";
            case VOLUNTEER -> "redirect:/volunteer/dashboard";
            case NEEDER -> "redirect:/needer/dashboard";
        };
    }
    
    @GetMapping("/resources")
    public String resources(Model model, 
                           @RequestParam(required = false) Resource.ResourceType type,
                           @RequestParam(required = false) String location) {
        List<Resource> resources;
        
        if (type != null || (location != null && !location.isEmpty())) {
            resources = resourceService.findFilteredResources(type, location);
        } else {
            resources = resourceService.findAvailableResources();
        }
        
        model.addAttribute("resources", resources);
        model.addAttribute("resourceTypes", Resource.ResourceType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedLocation", location);
        
        return "resources";
    }
    
    @GetMapping("/requests")
    public String requests(Model model,
                          @RequestParam(required = false) Request.UrgencyLevel urgency,
                          @RequestParam(required = false) String location) {
        List<Request> requests;
        
        if (urgency != null || (location != null && !location.isEmpty())) {
            requests = requestService.findFilteredRequests(urgency, location);
        } else {
            requests = requestService.findOpenRequests();
        }
        
        model.addAttribute("requests", requests);
        model.addAttribute("urgencyLevels", Request.UrgencyLevel.values());
        model.addAttribute("selectedUrgency", urgency);
        model.addAttribute("selectedLocation", location);
        
        return "requests";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

        @GetMapping("/history")
        public String history(Model model,
                  @RequestParam(required = false) String location) {
        List<ResourceAssignment> deliveredAssignments = resourceAssignmentService
            .findByStatus(ResourceAssignment.DeliveryStatus.DELIVERED)
            .stream()
            .filter(assignment -> location == null || location.isBlank() ||
                assignment.getResource().getLocation().toLowerCase().contains(location.toLowerCase()))
            .sorted(Comparator.comparing(ResourceAssignment::getDeliveredAt,
                Comparator.nullsLast(Comparator.reverseOrder())))
            .toList();

        model.addAttribute("deliveredAssignments", deliveredAssignments);
        model.addAttribute("selectedLocation", location);

        return "history";
        }
    
    @GetMapping("/access-denied")
    public String accessDenied(Model model, Principal principal) {
        if (principal != null) {
            var user = userService.findByUsername(principal.getName());
            user.ifPresent(value -> model.addAttribute("user", value));
        }
        return "access-denied";
    }
}