package com.sara.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sara.entity.Request;
import com.sara.entity.Resource;
import com.sara.entity.User;
import com.sara.service.RequestService;
import com.sara.service.ResourceService;
import com.sara.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/needer")
@PreAuthorize("hasRole('NEEDER') or hasRole('ADMIN')")
public class NeederController {
    
    private final RequestService requestService;
    private final ResourceService resourceService;
    private final UserService userService;
    
    public NeederController(RequestService requestService, ResourceService resourceService, UserService userService) {
        this.requestService = requestService;
        this.resourceService = resourceService;
        this.userService = userService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("userRequests", requestService.findByUser(user.get()));
            model.addAttribute("availableResources", resourceService.findAvailableResources().stream().limit(10).toList());
            model.addAttribute("totalRequests", requestService.findByUser(user.get()).size());
            model.addAttribute("fulfilledRequests", 
                requestService.findByUser(user.get()).stream()
                    .filter(r -> r.getStatus() == Request.RequestStatus.FULFILLED)
                    .count());
        }
        return "needer/dashboard";
    }
    
    @GetMapping("/requests")
    public String requests(Model model, Principal principal) {
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            model.addAttribute("requests", requestService.findByUser(user.get()));
        }
        return "needer/requests";
    }
    
    @GetMapping("/requests/new")
    public String newRequestForm(Model model) {
        model.addAttribute("request", new Request());
        model.addAttribute("resourceTypes", Resource.ResourceType.values());
        model.addAttribute("urgencyLevels", Request.UrgencyLevel.values());
        return "needer/request-form";
    }
    
    @PostMapping("/requests")
    public String createRequest(@Valid @ModelAttribute("request") Request request,
                               BindingResult result,
                               Model model,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("resourceTypes", Resource.ResourceType.values());
            model.addAttribute("urgencyLevels", Request.UrgencyLevel.values());
            return "needer/request-form";
        }
        
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            request.setUser(user.get());
            requestService.saveRequest(request);
            redirectAttributes.addFlashAttribute("success", "Request submitted successfully!");
        }
        
        return "redirect:/needer/requests";
    }
    
    @GetMapping("/requests/{id}/edit")
    public String editRequestForm(@PathVariable Long id, Model model, Principal principal) {
        Optional<Request> request = requestService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (request.isPresent() && user.isPresent()) {
            // Check if the request belongs to the current user
            if (!request.get().getUser().getId().equals(user.get().getId())) {
                return "redirect:/needer/requests";
            }
            
            model.addAttribute("request", request.get());
            model.addAttribute("resourceTypes", Resource.ResourceType.values());
            model.addAttribute("urgencyLevels", Request.UrgencyLevel.values());
            return "needer/request-form";
        }
        
        return "redirect:/needer/requests";
    }
    
    @PostMapping("/requests/{id}/edit")
    public String updateRequest(@PathVariable Long id,
                               @Valid @ModelAttribute("request") Request request,
                               BindingResult result,
                               Model model,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("resourceTypes", Resource.ResourceType.values());
            model.addAttribute("urgencyLevels", Request.UrgencyLevel.values());
            return "needer/request-form";
        }
        
        Optional<Request> existingRequest = requestService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (existingRequest.isPresent() && user.isPresent()) {
            // Check if the request belongs to the current user
            if (!existingRequest.get().getUser().getId().equals(user.get().getId())) {
                return "redirect:/needer/requests";
            }
            
            Request updatedRequest = existingRequest.get();
            updatedRequest.setTitle(request.getTitle());
            updatedRequest.setDescription(request.getDescription());
            updatedRequest.setResourceType(request.getResourceType());
            updatedRequest.setQuantityNeeded(request.getQuantityNeeded());
            updatedRequest.setLocation(request.getLocation());
            updatedRequest.setUrgency(request.getUrgency());
            updatedRequest.setNeededBy(request.getNeededBy());
            
            requestService.updateRequest(updatedRequest);
            redirectAttributes.addFlashAttribute("success", "Request updated successfully!");
        }
        
        return "redirect:/needer/requests";
    }
    
    @PostMapping("/requests/{id}/delete")
    public String deleteRequest(@PathVariable Long id, 
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        
        Optional<Request> request = requestService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (request.isPresent() && user.isPresent()) {
            // Check if the request belongs to the current user
            if (request.get().getUser().getId().equals(user.get().getId())) {
                requestService.deleteRequest(id);
                redirectAttributes.addFlashAttribute("success", "Request deleted successfully!");
            }
        }
        
        return "redirect:/needer/requests";
    }
    
    @GetMapping("/resources")
    public String availableResources(Model model,
                                    @RequestParam(required = false) Resource.ResourceType type,
                                    @RequestParam(required = false) String location) {
        var resources = (type != null || (location != null && !location.isEmpty())) 
            ? resourceService.findFilteredResources(type, location)
            : resourceService.findAvailableResources();
            
        model.addAttribute("resources", resources);
        model.addAttribute("resourceTypes", Resource.ResourceType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedLocation", location);
        
        return "needer/resources";
    }
    
    @GetMapping("/resources/{id}")
    public String viewResource(@PathVariable Long id, Model model) {
        Optional<Resource> resource = resourceService.findById(id);
        
        if (resource.isPresent()) {
            model.addAttribute("resource", resource.get());
            return "needer/resources_view";
        }
        
        return "redirect:/needer/resources";
    }
    
    @GetMapping("/requests/{id}")
    public String viewRequest(@PathVariable Long id, Model model, Principal principal) {
        Optional<Request> request = requestService.findById(id);
        Optional<User> user = userService.findByUsername(principal.getName());
        
        if (request.isPresent() && user.isPresent()) {
            // Check if the request belongs to the current user
            if (!request.get().getUser().getId().equals(user.get().getId())) {
                return "redirect:/needer/requests";
            }
            
            model.addAttribute("request", request.get());
            return "needer/requests_view";
        }
        
        return "redirect:/needer/requests";
    }
}