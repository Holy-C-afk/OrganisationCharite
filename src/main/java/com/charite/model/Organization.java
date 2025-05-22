package com.charite.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "organizations")
public class Organization {
    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String legalRegistrationNumber;

    private String address;
    private String contactEmail;
    private String contactPhone;
    private String website;
    private String logo;
    private String description;

    private OrganizationStatus status = OrganizationStatus.PENDING;

    private Set<String> charityActionIds = new HashSet<>();
    private Set<String> adminIds = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    public enum OrganizationStatus {
        PENDING,
        APPROVED,
        REJECTED,
        SUSPENDED
    }
} 