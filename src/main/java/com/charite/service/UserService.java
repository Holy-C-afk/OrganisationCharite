package com.charite.service;

import com.charite.model.User;
import com.charite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getRecentUsers(int limit) {
        return userRepository.findAll().stream()
            .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        logger.info("Creating new user with email: {}", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Email already exists: {}", user.getEmail());
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    public Optional<User> updateUser(Long id, User user) {
        logger.info("Updating user with ID: {}", id);
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setPhoneNumber(user.getPhoneNumber());
                    existingUser.setAddress(user.getAddress());
                    existingUser.setProfilePicture(user.getProfilePicture());
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    User updatedUser = userRepository.save(existingUser);
                    logger.info("User updated successfully");
                    return updatedUser;
                });
    }

    public boolean deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("User deleted successfully");
            return true;
        }
        logger.warn("User not found for deletion: {}", id);
        return false;
    }

    public User registerUser(User user) {
        logger.info("Registering new user with email: {}", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Email already exists: {}", user.getEmail());
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    public void updateUserRole(Long id, User.UserRole role) {
        logger.info("Updating role for user ID: {} to role: {}", id, role);
        userRepository.findById(id).ifPresent(user -> {
            user.setRole(role);
            userRepository.save(user);
            logger.info("User role updated successfully");
        });
    }

    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<User> users = userRepository.findAll();
        
        stats.put("totalUsers", users.size());
        stats.put("activeUsers", users.stream()
            .filter(u -> u.getLastLogin() != null && 
                u.getLastLogin().isAfter(LocalDateTime.now().minusDays(30)))
            .count());
        stats.put("usersByRole", users.stream()
            .collect(Collectors.groupingBy(
                User::getRole,
                Collectors.counting()
            )));
        
        return stats;
    }
} 