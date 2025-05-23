package com.charite.config;

import com.charite.model.User;
import com.charite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        logger.info("Initializing application data...");
        
        // Create admin user if not exists
        try {
            var adminUserOpt = userService.getUserByEmail("admin@charite.com");
            if (adminUserOpt.isEmpty()) {
                User adminUser = new User();
                adminUser.setEmail("admin@charite.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setRole(User.UserRole.SUPER_ADMIN);
                userService.createUser(adminUser);
                logger.info("Admin user created successfully");
            } else {
                User adminUser = adminUserOpt.get();
                if (adminUser.getRole() != User.UserRole.SUPER_ADMIN) {
                    userService.updateUserRole(adminUser.getId(), User.UserRole.SUPER_ADMIN);
                    logger.info("Admin user role updated to SUPER_ADMIN");
                }
            }
        } catch (Exception e) {
            logger.error("Error creating admin user", e);
        }
        
        logger.info("Data initialization completed");
    }
} 