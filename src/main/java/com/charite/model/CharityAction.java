package com.charite.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "charity_actions")
public class CharityAction {
    @Id
    private String id;

    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double targetAmount;
    private double currentAmount;
    private String status;
    private String imageUrl;
    private String videoUrl;

    private String organizationId;
    private Set<String> donationIds = new HashSet<>();
    private Set<String> participantIds = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status {
        PLANNED,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }
} 