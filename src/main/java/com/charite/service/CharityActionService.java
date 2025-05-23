package com.charite.service;

import com.charite.model.CharityAction;
import com.charite.model.User;
import com.charite.repository.CharityActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CharityActionService {
    private static final Logger logger = LoggerFactory.getLogger(CharityActionService.class);

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

    public List<CharityAction> getAllActions() {
        return charityActionRepository.findAll();
    }

    public List<CharityAction> getRecentActions(int limit) {
        return charityActionRepository.findAll().stream()
            .sorted((a1, a2) -> a2.getCreatedAt().compareTo(a1.getCreatedAt()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<CharityAction> getActionsByStatus(CharityAction.ActionStatus status) {
        return charityActionRepository.findByStatus(status);
    }

    public List<CharityAction> getOrganizerActions(User organizer) {
        return charityActionRepository.findByOrganizer(organizer);
    }

    public Optional<CharityAction> getActionById(Long id) {
        return charityActionRepository.findById(id);
    }

    @Transactional
    public CharityAction createAction(String title, String description, CharityAction.ActionCategory category,
                                    String location, LocalDate date, MultipartFile image, User organizer) throws IOException {
        logger.info("Creating new charity action: {}", title);
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

        CharityAction savedAction = charityActionRepository.save(action);
        logger.info("Charity action created successfully with ID: {}", savedAction.getId());
        return savedAction;
    }

    public Optional<CharityAction> updateAction(Long id, CharityAction action) {
        logger.info("Updating charity action with ID: {}", id);
        return charityActionRepository.findById(id)
                .map(existingAction -> {
                    existingAction.setTitle(action.getTitle());
                    existingAction.setDescription(action.getDescription());
                    existingAction.setCategory(action.getCategory());
                    existingAction.setLocation(action.getLocation());
                    existingAction.setDate(action.getDate());
                    CharityAction updatedAction = charityActionRepository.save(existingAction);
                    logger.info("Charity action updated successfully");
                    return updatedAction;
                });
    }

    public void updateActionStatus(Long id, CharityAction.ActionStatus status) {
        logger.info("Updating status for charity action ID: {} to status: {}", id, status);
        charityActionRepository.findById(id).ifPresent(action -> {
            action.setStatus(status);
            charityActionRepository.save(action);
            logger.info("Charity action status updated successfully");
        });
    }

    public boolean deleteAction(Long id) {
        logger.info("Deleting charity action with ID: {}", id);
        if (charityActionRepository.existsById(id)) {
            charityActionRepository.deleteById(id);
            logger.info("Charity action deleted successfully");
            return true;
        }
        logger.warn("Charity action not found for deletion: {}", id);
        return false;
    }

    public Map<String, Object> getActionStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<CharityAction> actions = charityActionRepository.findAll();
        
        stats.put("totalActions", actions.size());
        stats.put("actionsByStatus", actions.stream()
            .collect(Collectors.groupingBy(
                CharityAction::getStatus,
                Collectors.counting()
            )));
        stats.put("actionsByCategory", actions.stream()
            .collect(Collectors.groupingBy(
                CharityAction::getCategory,
                Collectors.counting()
            )));
        
        return stats;
    }

    public void addParticipant(Long actionId, String fullName, String email, String phone, String availability, String message) {
        logger.info("Adding participant to charity action ID: {}", actionId);
        CharityAction action = charityActionRepository.findById(actionId)
            .orElseThrow(() -> new RuntimeException("Action caritative non trouvée"));

        // Vérifier si l'action est active ou à venir
        if (action.getStatus() != CharityAction.ActionStatus.ACTIVE && 
            action.getStatus() != CharityAction.ActionStatus.UPCOMING) {
            throw new RuntimeException("Cette action n'accepte plus de participants");
        }

        // TODO: Implémenter la logique de stockage des participants
        // Pour l'instant, on se contente de logger l'information
        logger.info("Nouveau participant pour l'action {}: {} ({})", 
            action.getTitle(), fullName, email);
    }
} 