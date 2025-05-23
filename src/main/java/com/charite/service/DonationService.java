package com.charite.service;

import com.charite.model.Donation;
import com.charite.model.User;
import com.charite.model.CharityAction;
import com.charite.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class DonationService {
    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    @Autowired
    private DonationRepository donationRepository;

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public List<Donation> getRecentDonations(int limit) {
        return donationRepository.findAll().stream()
            .sorted((d1, d2) -> d2.getCreatedAt().compareTo(d1.getCreatedAt()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<Donation> getUserDonations(User user) {
        return donationRepository.findByUser(user);
    }

    public List<Donation> getCharityActionDonations(CharityAction charityAction) {
        return donationRepository.findByCharityAction(charityAction);
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    public List<Donation> getDonationsByStatus(Donation.Status status) {
        return donationRepository.findByStatus(status);
    }

    @Transactional
    public Donation createDonation(BigDecimal amount, Donation.DonationCategory category,
                                 String message, User user) {
        logger.info("Creating new donation for user: {}", user.getEmail());
        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setCategory(category);
        donation.setMessage(message);
        donation.setCreatedAt(LocalDateTime.now());
        donation.setUser(user);
        donation.setStatus(Donation.Status.PENDING);
        
        Donation savedDonation = donationRepository.save(donation);
        logger.info("Donation created successfully with ID: {}", savedDonation.getId());
        return savedDonation;
    }

    public Optional<Donation> updateDonation(Long id, Donation donation) {
        return donationRepository.findById(id)
                .map(existingDonation -> {
                    existingDonation.setAmount(donation.getAmount());
                    existingDonation.setCategory(donation.getCategory());
                    existingDonation.setMessage(donation.getMessage());
                    existingDonation.setStatus(donation.getStatus());
                    return donationRepository.save(existingDonation);
                });
    }

    public boolean deleteDonation(Long id) {
        if (donationRepository.existsById(id)) {
            donationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Donation> getDonationsByCategory(Donation.DonationCategory category) {
        return donationRepository.findByCategory(category);
    }

    public BigDecimal getTotalDonationsAmount(User user) {
        return donationRepository.findByUser(user).stream()
            .filter(d -> d.getStatus() == Donation.Status.COMPLETED)
            .map(Donation::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void updateDonationStatus(Long id, Donation.Status status) {
        logger.info("Updating status for donation ID: {} to status: {}", id, status);
        donationRepository.findById(id).ifPresent(donation -> {
            donation.setStatus(status);
            donationRepository.save(donation);
            logger.info("Donation status updated successfully");
        });
    }

    public Map<String, Object> getDonationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<Donation> donations = donationRepository.findAll();
        
        stats.put("totalDonations", donations.size());
        stats.put("totalAmount", donations.stream()
            .filter(d -> d.getStatus() == Donation.Status.COMPLETED)
            .map(Donation::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.put("donationsByStatus", donations.stream()
            .collect(Collectors.groupingBy(
                Donation::getStatus,
                Collectors.counting()
            )));
        stats.put("donationsByCategory", donations.stream()
            .collect(Collectors.groupingBy(
                Donation::getCategory,
                Collectors.counting()
            )));
        
        return stats;
    }
} 