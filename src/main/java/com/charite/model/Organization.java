package com.charite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String legalRegistrationNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contactEmail;

    @Column(nullable = false)
    private String contactPhone;

    private String website;
    private String logo;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationStatus status = OrganizationStatus.PENDING;

    @ElementCollection
    @CollectionTable(name = "organization_charity_actions", joinColumns = @JoinColumn(name = "organization_id"))
    @Column(name = "charity_action_id")
    private Set<Long> charityActionIds = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "organization_admins", joinColumns = @JoinColumn(name = "organization_id"))
    @Column(name = "admin_id")
    private Set<Long> adminIds = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastUpdated;

    public enum OrganizationStatus {
        PENDING,
        APPROVED,
        REJECTED,
        SUSPENDED
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalRegistrationNumber() {
        return legalRegistrationNumber;
    }

    public void setLegalRegistrationNumber(String legalRegistrationNumber) {
        this.legalRegistrationNumber = legalRegistrationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrganizationStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizationStatus status) {
        this.status = status;
    }

    public Set<Long> getCharityActionIds() {
        return charityActionIds;
    }

    public void setCharityActionIds(Set<Long> charityActionIds) {
        this.charityActionIds = charityActionIds;
    }

    public Set<Long> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(Set<Long> adminIds) {
        this.adminIds = adminIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
} 