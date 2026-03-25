package com.sara.repository;

import com.sara.entity.Resource;
import com.sara.entity.ResourceAssignment;
import com.sara.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceAssignmentRepository extends JpaRepository<ResourceAssignment, Long> {
    List<ResourceAssignment> findByVolunteer(User volunteer);
    List<ResourceAssignment> findByResource(Resource resource);
    List<ResourceAssignment> findByStatus(ResourceAssignment.DeliveryStatus status);
    List<ResourceAssignment> findByVolunteerOrderByAssignedAtDesc(User volunteer);
    Optional<ResourceAssignment> findByResourceAndStatus(Resource resource, ResourceAssignment.DeliveryStatus status);
}
