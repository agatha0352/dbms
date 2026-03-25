package com.sara.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resource_assignments")
public class ResourceAssignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    @NotNull
    private Resource resource;
    
    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    @NotNull
    private User volunteer;
    
    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.ASSIGNED;
    
    @Column(length = 500)
    private String notes;
    
    public enum DeliveryStatus {
        ASSIGNED,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED
    }
    
    // Constructors
    public ResourceAssignment() {}
    
    public ResourceAssignment(Resource resource, User volunteer) {
        this.resource = resource;
        this.volunteer = volunteer;
        this.status = DeliveryStatus.ASSIGNED;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Resource getResource() {
        return resource;
    }
    
    public void setResource(Resource resource) {
        this.resource = resource;
    }
    
    public User getVolunteer() {
        return volunteer;
    }
    
    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
    }
    
    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
    
    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
    
    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }
    
    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
    
    public DeliveryStatus getStatus() {
        return status;
    }
    
    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
