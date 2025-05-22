package com.charite.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String phoneNumber;
    private String address;
    private String profilePicture;

    private UserRole role = UserRole.USER;

    private String organizationId;

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    private Set<String> donationIds = new HashSet<>();

    public enum UserRole {
        USER,
        ORGANIZATION_ADMIN,
        SUPER_ADMIN
    }
} 