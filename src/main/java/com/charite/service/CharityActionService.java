package com.charite.service;

import com.charite.model.CharityAction;
import com.charite.repository.CharityActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CharityActionService {

    @Autowired
    private CharityActionRepository charityActionRepository;

    public List<CharityAction> getAllCharityActions() {
        return charityActionRepository.findAll();
    }

    public Optional<CharityAction> getCharityActionById(String id) {
        return charityActionRepository.findById(id);
    }

    public List<CharityAction> getCharityActionsByOrganization(String organizationId) {
        return charityActionRepository.findByOrganizationId(organizationId);
    }

    public List<CharityAction> getCharityActionsByStatus(String status) {
        return charityActionRepository.findByStatus(status);
    }

    public List<CharityAction> getCharityActionsByCategory(String category) {
        return charityActionRepository.findByCategory(category);
    }

    public CharityAction createCharityAction(CharityAction charityAction) {
        charityAction.setCreatedAt(LocalDateTime.now());
        charityAction.setUpdatedAt(LocalDateTime.now());
        return charityActionRepository.save(charityAction);
    }

    public Optional<CharityAction> updateCharityAction(String id, CharityAction charityAction) {
        return charityActionRepository.findById(id)
                .map(existingAction -> {
                    existingAction.setTitle(charityAction.getTitle());
                    existingAction.setDescription(charityAction.getDescription());
                    existingAction.setCategory(charityAction.getCategory());
                    existingAction.setLocation(charityAction.getLocation());
                    existingAction.setStartDate(charityAction.getStartDate());
                    existingAction.setEndDate(charityAction.getEndDate());
                    existingAction.setTargetAmount(charityAction.getTargetAmount());
                    existingAction.setStatus(charityAction.getStatus());
                    existingAction.setImageUrl(charityAction.getImageUrl());
                    existingAction.setVideoUrl(charityAction.getVideoUrl());
                    existingAction.setUpdatedAt(LocalDateTime.now());
                    return charityActionRepository.save(existingAction);
                });
    }

    public boolean deleteCharityAction(String id) {
        if (charityActionRepository.existsById(id)) {
            charityActionRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 