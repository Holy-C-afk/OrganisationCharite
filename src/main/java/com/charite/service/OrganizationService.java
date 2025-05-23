package com.charite.service;

import com.charite.model.Organization;
import com.charite.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class OrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public List<Organization> getRecentOrganizations(int limit) {
        return organizationRepository.findAll().stream()
            .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public Optional<Organization> getOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    public Organization createOrganization(Organization organization) {
        logger.info("Creating new organization: {}", organization.getName());
        organization.setCreatedAt(LocalDateTime.now());
        organization.setStatus(Organization.OrganizationStatus.PENDING);
        Organization savedOrg = organizationRepository.save(organization);
        logger.info("Organization created successfully with ID: {}", savedOrg.getId());
        return savedOrg;
    }

    public Optional<Organization> updateOrganization(Long id, Organization organization) {
        logger.info("Updating organization with ID: {}", id);
        return organizationRepository.findById(id)
                .map(existingOrg -> {
                    existingOrg.setName(organization.getName());
                    existingOrg.setAddress(organization.getAddress());
                    existingOrg.setContactEmail(organization.getContactEmail());
                    existingOrg.setContactPhone(organization.getContactPhone());
                    existingOrg.setWebsite(organization.getWebsite());
                    existingOrg.setDescription(organization.getDescription());
                    existingOrg.setLastUpdated(LocalDateTime.now());
                    Organization updatedOrg = organizationRepository.save(existingOrg);
                    logger.info("Organization updated successfully");
                    return updatedOrg;
                });
    }

    public void updateOrganizationStatus(Long id, Organization.OrganizationStatus status) {
        logger.info("Updating status for organization ID: {} to status: {}", id, status);
        organizationRepository.findById(id).ifPresent(org -> {
            org.setStatus(status);
            org.setLastUpdated(LocalDateTime.now());
            organizationRepository.save(org);
            logger.info("Organization status updated successfully");
        });
    }

    public boolean deleteOrganization(Long id) {
        logger.info("Deleting organization with ID: {}", id);
        if (organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
            logger.info("Organization deleted successfully");
            return true;
        }
        logger.warn("Organization not found for deletion: {}", id);
        return false;
    }

    public Map<String, Object> getOrganizationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<Organization> organizations = organizationRepository.findAll();
        
        stats.put("totalOrganizations", organizations.size());
        stats.put("organizationsByStatus", organizations.stream()
            .collect(Collectors.groupingBy(
                Organization::getStatus,
                Collectors.counting()
            )));
        
        return stats;
    }
} 