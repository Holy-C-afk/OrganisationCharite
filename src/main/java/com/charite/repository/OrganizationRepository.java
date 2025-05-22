package com.charite.repository;

import com.charite.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByLegalRegistrationNumber(String legalRegistrationNumber);
    List<Organization> findByStatus(Organization.OrganizationStatus status);
    boolean existsByLegalRegistrationNumber(String legalRegistrationNumber);
} 