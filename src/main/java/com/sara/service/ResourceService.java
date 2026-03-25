package com.sara.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sara.entity.Resource;
import com.sara.entity.User;
import com.sara.repository.ResourceRepository;

@Service
@Transactional
public class ResourceService {
    
    private final ResourceRepository resourceRepository;
    
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }
    
    public Resource saveResource(Resource resource) {
        applyExpiryStatus(resource);
        return resourceRepository.save(resource);
    }
    
    public Optional<Resource> findById(Long id) {
        Optional<Resource> resourceOpt = resourceRepository.findById(id);
        if (resourceOpt.isPresent() && applyExpiryStatus(resourceOpt.get())) {
            resourceRepository.save(resourceOpt.get());
        }
        return resourceOpt;
    }
    
    public List<Resource> findAllResources() {
        List<Resource> resources = resourceRepository.findAll();
        refreshExpiredStatuses(resources);
        return resources;
    }
    
    public List<Resource> findByUser(User user) {
        List<Resource> resources = resourceRepository.findByUserOrderByCreatedAtDesc(user);
        refreshExpiredStatuses(resources);
        return resources;
    }
    
    public List<Resource> findAvailableResources() {
        List<Resource> resources = resourceRepository.findAvailableVerifiedResources();
        refreshExpiredStatuses(resources);
        return resources.stream()
            .filter(resource -> resource.getStatus() == Resource.ResourceStatus.AVAILABLE)
            .toList();
    }
    
    public List<Resource> findByType(Resource.ResourceType type) {
        List<Resource> resources = resourceRepository.findByType(type);
        refreshExpiredStatuses(resources);
        return resources;
    }
    
    public List<Resource> findByStatus(Resource.ResourceStatus status) {
        List<Resource> resources = resourceRepository.findByStatus(status);
        refreshExpiredStatuses(resources);
        return resources;
    }
    
    public List<Resource> findVerifiedResources() {
        List<Resource> resources = resourceRepository.findByVerifiedTrue();
        refreshExpiredStatuses(resources);
        return resources;
    }
    
    public List<Resource> findFilteredResources(Resource.ResourceType type, String location) {
        List<Resource> resources = resourceRepository.findFilteredResources(type, location);
        refreshExpiredStatuses(resources);
        return resources;
    }
    
    public Resource updateResource(Resource resource) {
        applyExpiryStatus(resource);
        return resourceRepository.save(resource);
    }
    
    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
    
    public void verifyResource(Long id) {
        Optional<Resource> resourceOpt = resourceRepository.findById(id);
        if (resourceOpt.isPresent()) {
            Resource resource = resourceOpt.get();
            resource.setVerified(true);
            resourceRepository.save(resource);
        }
    }
    
    public void updateResourceStatus(Long id, Resource.ResourceStatus status) {
        Optional<Resource> resourceOpt = resourceRepository.findById(id);
        if (resourceOpt.isPresent()) {
            Resource resource = resourceOpt.get();
            resource.setStatus(status);
            applyExpiryStatus(resource);
            resourceRepository.save(resource);
        }
    }
    
    public long getTotalResourcesCount() {
        return resourceRepository.count();
    }
    
    public long getAvailableResourcesCount() {
        return findAvailableResources().size();
    }

    private boolean applyExpiryStatus(Resource resource) {
        if (resource.getExpiryDate() == null) {
            return false;
        }

        if (resource.getExpiryDate().isBefore(LocalDate.now())
                && resource.getStatus() != Resource.ResourceStatus.DELIVERED
                && resource.getStatus() != Resource.ResourceStatus.EXPIRED) {
            resource.setStatus(Resource.ResourceStatus.EXPIRED);
            return true;
        }

        return false;
    }

    private void refreshExpiredStatuses(List<Resource> resources) {
        List<Resource> changedResources = new ArrayList<>();

        for (Resource resource : resources) {
            if (applyExpiryStatus(resource)) {
                changedResources.add(resource);
            }
        }

        if (!changedResources.isEmpty()) {
            resourceRepository.saveAll(changedResources);
        }
    }
}