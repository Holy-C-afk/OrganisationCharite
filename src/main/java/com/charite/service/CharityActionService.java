package com.charite.service;

import com.charite.model.CharityAction;
import com.charite.model.User;
import com.charite.repository.CharityActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CharityActionService {

    @Autowired
    private CharityActionRepository charityActionRepository;

    private final Path uploadDir = Paths.get(System.getProperty("java.io.tmpdir"), "charity-actions");

    public CharityActionService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @Transactional
    public CharityAction createAction(String title, String description, CharityAction.ActionCategory category,
                                    String location, LocalDate date, MultipartFile image, User organizer) throws IOException {
        CharityAction action = new CharityAction();
        action.setTitle(title);
        action.setDescription(description);
        action.setCategory(category);
        action.setLocation(location);
        action.setDate(date);
        action.setOrganizer(organizer);
        action.setCreatedAt(LocalDateTime.now());

        // Set initial status based on date
        if (date.isBefore(LocalDate.now())) {
            action.setStatus(CharityAction.ActionStatus.COMPLETED);
        } else if (date.equals(LocalDate.now())) {
            action.setStatus(CharityAction.ActionStatus.ACTIVE);
        } else {
            action.setStatus(CharityAction.ActionStatus.UPCOMING);
        }

        // Handle image upload
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);
            action.setImageUrl("/uploads/charity-actions/" + fileName);
        }

        return charityActionRepository.save(action);
    }

    public List<CharityAction> getOrganizerActions(User organizer) {
        return charityActionRepository.findByOrganizerId(organizer.getId());
    }

    public List<CharityAction> getActionsByCategory(CharityAction.ActionCategory category) {
        return charityActionRepository.findByCategory(category);
    }

    public List<CharityAction> getActionsByStatus(CharityAction.ActionStatus status) {
        return charityActionRepository.findByStatus(status);
    }

    @Transactional
    public void updateActionStatus(Long actionId, CharityAction.ActionStatus newStatus) {
        charityActionRepository.findById(actionId).ifPresent(action -> {
            action.setStatus(newStatus);
            charityActionRepository.save(action);
        });
    }
} 