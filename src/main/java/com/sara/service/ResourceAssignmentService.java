package com.sara.service;

import com.sara.entity.Resource;
import com.sara.entity.ResourceAssignment;
import com.sara.entity.User;
import com.sara.repository.ResourceAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResourceAssignmentService {
    
    private final ResourceAssignmentRepository assignmentRepository;
    private final ResourceService resourceService;
    
    public ResourceAssignmentService(ResourceAssignmentRepository assignmentRepository, 
                                    ResourceService resourceService) {
        this.assignmentRepository = assignmentRepository;
        this.resourceService = resourceService;
    }
    
    public ResourceAssignment assignResourceToVolunteer(Resource resource, User volunteer, String notes) {
        // Check if resource is already assigned
        Optional<ResourceAssignment> existing = assignmentRepository.findByResourceAndStatus(
            resource, ResourceAssignment.DeliveryStatus.ASSIGNED);
        
        if (existing.isPresent()) {
            throw new IllegalStateException("Resource is already assigned to another volunteer");
        }
        
        // Check if resource is verified and available
        if (!resource.isVerified()) {
            throw new IllegalStateException("Resource must be verified before assignment");
        }
        
        if (resource.getStatus() != Resource.ResourceStatus.AVAILABLE) {
            throw new IllegalStateException("Resource is not available for assignment");
        }
        
        // Create assignment
        ResourceAssignment assignment = new ResourceAssignment(resource, volunteer);
        assignment.setNotes(notes);
        
        // Update resource status to RESERVED
        resourceService.updateResourceStatus(resource.getId(), Resource.ResourceStatus.RESERVED);
        
        return assignmentRepository.save(assignment);
    }
    
    public void updateDeliveryStatus(Long assignmentId, ResourceAssignment.DeliveryStatus status) {
        Optional<ResourceAssignment> assignmentOpt = assignmentRepository.findById(assignmentId);
        
        if (assignmentOpt.isPresent()) {
            ResourceAssignment assignment = assignmentOpt.get();
            assignment.setStatus(status);
            
            // If delivered, update timestamp and resource status
            if (status == ResourceAssignment.DeliveryStatus.DELIVERED) {
                assignment.setDeliveredAt(LocalDateTime.now());
                resourceService.updateResourceStatus(
                    assignment.getResource().getId(), 
                    Resource.ResourceStatus.DELIVERED
                );
            }
            
            // If cancelled, make resource available again
            if (status == ResourceAssignment.DeliveryStatus.CANCELLED) {
                resourceService.updateResourceStatus(
                    assignment.getResource().getId(), 
                    Resource.ResourceStatus.AVAILABLE
                );
            }
            
            assignmentRepository.save(assignment);
        }
    }
    
    public List<ResourceAssignment> findByVolunteer(User volunteer) {
        return assignmentRepository.findByVolunteerOrderByAssignedAtDesc(volunteer);
    }
    
    public List<ResourceAssignment> findByResource(Resource resource) {
        return assignmentRepository.findByResource(resource);
    }
    
    public List<ResourceAssignment> findByStatus(ResourceAssignment.DeliveryStatus status) {
        return assignmentRepository.findByStatus(status);
    }
    
    public List<ResourceAssignment> findAll() {
        return assignmentRepository.findAll();
    }
    
    public Optional<ResourceAssignment> findById(Long id) {
        return assignmentRepository.findById(id);
    }
    
    public void deleteAssignment(Long id) {
        Optional<ResourceAssignment> assignment = assignmentRepository.findById(id);
        if (assignment.isPresent()) {
            // Make resource available again
            resourceService.updateResourceStatus(
                assignment.get().getResource().getId(), 
                Resource.ResourceStatus.AVAILABLE
            );
            assignmentRepository.deleteById(id);
        }
    }
}
