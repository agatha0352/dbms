package com.sara.controller;

import com.sara.entity.Request;
import com.sara.entity.Resource;
import com.sara.entity.ResourceAssignment;
import com.sara.entity.User;
import com.sara.entity.VolunteerAssignment;
import com.sara.service.RequestService;
import com.sara.service.ResourceAssignmentService;
import com.sara.service.ResourceService;
import com.sara.service.UserService;
import com.sara.service.VolunteerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/volunteer")
@PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
public class VolunteerController {
    
    private final RequestService requestService;
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final ResourceAssignmentService resourceAssignmentService;
    private final ResourceService resourceService;
    
    public VolunteerController(RequestService requestService, UserService userService, 
                              VolunteerService volunteerService,
                              ResourceAssignmentService resourceAssignmentService,
                              ResourceService resourceService) {
        this.requestService = requestService;
        this.userService = userService;
        this.volunteerService = volunteerService;
        this.resourceAssignmentService = resourceAssignmentService;
        this.resourceService = resourceService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            var currentUser = user.get();
            var myAssignments = volunteerService.findByVolunteer(currentUser);
            var availableRequests = requestService.findOpenRequests().stream().limit(10).toList();

            model.addAttribute("user", currentUser);
            model.addAttribute("myAssignments", myAssignments);
            model.addAttribute("availableRequests", availableRequests);
            model.addAttribute("totalAssignments", myAssignments != null ? myAssignments.size() : 0);
            model.addAttribute("completedAssignments",
                    myAssignments != null ? myAssignments.stream()
                            .filter(a -> a.getStatus() == VolunteerAssignment.AssignmentStatus.COMPLETED)
                            .count() : 0);
        }
        return "volunteer/dashboard";
    }
    
    @GetMapping("/requests")
    public String availableRequests(Model model,
                                   @RequestParam(required = false) Request.UrgencyLevel urgency,
                                   @RequestParam(required = false) String location) {
        var requests = (urgency != null || (location != null && !location.isEmpty())) 
            ? requestService.findFilteredRequests(urgency, location)
            : requestService.findOpenRequests();
            
        model.addAttribute("requests", requests);
        model.addAttribute("urgencyLevels", Request.UrgencyLevel.values());
        model.addAttribute("selectedUrgency", urgency);
        model.addAttribute("selectedLocation", location);
        
        return "volunteer/requests";
    }
    
    @GetMapping("/assignments")
    public String myAssignments(Model model, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            model.addAttribute("assignments", volunteerService.findByVolunteer(user.get()));
        }
        return "volunteer/assignments";
    }
    
    @GetMapping("/requests/{id}/volunteer")
    public String volunteerForRequestForm(@PathVariable Long id, Model model, Principal principal) {
        Optional<Request> request = requestService.findById(id);
        if (request.isPresent()) {
            model.addAttribute("request", request.get());
            return "volunteer/volunteer_form";
        }
        return "redirect:/volunteer/requests";
    }
    
    @PostMapping("/requests/{id}/volunteer")
    public String volunteerForRequest(@PathVariable Long id,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {
        
        Optional<Request> request = requestService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (request.isPresent() && user.isPresent()) {
            try {
                volunteerService.assignVolunteer(user.get(), request.get());
                redirectAttributes.addFlashAttribute("success", 
                    "You have successfully volunteered for this request!");
            } catch (IllegalStateException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        }
        
        return "redirect:/volunteer/requests";
    }
    
    @PostMapping("/assignments/{id}/status")
    public String updateAssignmentStatus(@PathVariable Long id,
                                        @RequestParam VolunteerAssignment.AssignmentStatus status,
                                        RedirectAttributes redirectAttributes,
                                        Principal principal) {
        
        Optional<VolunteerAssignment> assignment = volunteerService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (assignment.isPresent() && user.isPresent()) {
            // Check if the assignment belongs to the current volunteer
            if (assignment.get().getVolunteer().getId().equals(user.get().getId())) {
                volunteerService.updateAssignmentStatus(id, status);
                
                // Update request status if assignment is completed
                if (status == VolunteerAssignment.AssignmentStatus.COMPLETED) {
                    requestService.updateRequestStatus(
                        assignment.get().getRequest().getId(), 
                        Request.RequestStatus.FULFILLED
                    );
                }
                
                redirectAttributes.addFlashAttribute("success", "Assignment status updated successfully!");
            }
        }
        
        return "redirect:/volunteer/assignments";
    }
    
    @PostMapping("/assignments/{id}/cancel")
    public String cancelAssignment(@PathVariable Long id,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        
        Optional<VolunteerAssignment> assignment = volunteerService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (assignment.isPresent() && user.isPresent()) {
            // Check if the assignment belongs to the current volunteer
            if (assignment.get().getVolunteer().getId().equals(user.get().getId())) {
                volunteerService.deleteAssignment(id);
                redirectAttributes.addFlashAttribute("success", "Assignment cancelled successfully!");
            }
        }
        
        return "redirect:/volunteer/assignments";
    }
    
    @GetMapping("/requests/{id}")
    public String viewRequest(@PathVariable Long id, Model model, Principal principal) {
        Optional<Request> request = requestService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (request.isPresent() && user.isPresent()) {
            model.addAttribute("request", request.get());
            model.addAttribute("isAssigned", volunteerService.isVolunteerAssigned(user.get(), request.get()));
            return "volunteer/requests_view";
        }
        
        return "redirect:/volunteer/requests";
    }
    
    // Resource Delivery endpoints
    @GetMapping("/deliveries")
    public String myDeliveries(Model model, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            model.addAttribute("deliveries", resourceAssignmentService.findByVolunteer(user.get()));
            model.addAttribute("deliveryStatuses", ResourceAssignment.DeliveryStatus.values());
        }
        return "volunteer/deliveries";
    }
    
    @PostMapping("/deliveries/{id}/status")
    public String updateDeliveryStatus(@PathVariable Long id,
                                      @RequestParam ResourceAssignment.DeliveryStatus status,
                                      RedirectAttributes redirectAttributes,
                                      Principal principal) {
        
        Optional<ResourceAssignment> delivery = resourceAssignmentService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (delivery.isPresent() && user.isPresent()) {
            // Check if the delivery belongs to the current volunteer
            if (delivery.get().getVolunteer().getId().equals(user.get().getId())) {
                resourceAssignmentService.updateDeliveryStatus(id, status);
                redirectAttributes.addFlashAttribute("success", "Delivery status updated successfully!");
            }
        }
        
        return "redirect:/volunteer/deliveries";
    }
    
    @PostMapping("/deliveries/{id}/cancel")
    public String cancelDelivery(@PathVariable Long id,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        
        Optional<ResourceAssignment> delivery = resourceAssignmentService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (delivery.isPresent() && user.isPresent()) {
            // Check if the delivery belongs to the current volunteer
            if (delivery.get().getVolunteer().getId().equals(user.get().getId())) {
                resourceAssignmentService.updateDeliveryStatus(id, ResourceAssignment.DeliveryStatus.CANCELLED);
                redirectAttributes.addFlashAttribute("success", "Delivery cancelled successfully!");
            }
        }
        
        return "redirect:/volunteer/deliveries";
    }
    
    // Resource Verification endpoints (volunteers verify pending resources)
    @GetMapping("/resources/pending")
    public String pendingResources(Model model) {
        // Get all unverified resources
        var pendingResources = resourceService.findAllResources().stream()
            .filter(r -> !r.isVerified())
            .toList();
        
        model.addAttribute("resources", pendingResources);
        return "volunteer/resources_pending";
    }
    
    @PostMapping("/resources/{id}/verify")
    public String verifyResource(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        resourceService.verifyResource(id);
        redirectAttributes.addFlashAttribute("success", "Resource verified successfully!");
        return "redirect:/volunteer/resources/pending";
    }
}
