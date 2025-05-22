package com.charite.service;

import com.charite.model.Donation;
import com.charite.model.User;
import com.charite.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    public List<Donation> getDonationsByUser(Long userId) {
        return donationRepository.findByUserId(userId);
    }

    public List<Donation> getDonationsByCharityAction(Long charityActionId) {
        return donationRepository.findByCharityActionId(charityActionId);
    }

    public List<Donation> getDonationsByStatus(Donation.Status status) {
        return donationRepository.findByStatus(status);
    }

    @Transactional
    public Donation createDonation(BigDecimal amount, Donation.DonationCategory category, String message, User user) {
        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setCategory(category);
        donation.setMessage(message);
        donation.setUser(user);
        donation.setCreatedAt(LocalDateTime.now());
        donation.setStatus(Donation.Status.PENDING);
        return donationRepository.save(donation);
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

    public List<Donation> getUserDonations(User user) {
        return donationRepository.findByUserId(user.getId());
    }

    public List<Donation> getDonationsByCategory(Donation.DonationCategory category) {
        return donationRepository.findByCategory(category);
    }

    public BigDecimal getTotalDonationsAmount(User user) {
        return donationRepository.findByUserId(user.getId())
                .stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void updateDonationStatus(Long id, Donation.Status status) {
        donationRepository.findById(id).ifPresent(donation -> {
            donation.setStatus(status);
            donationRepository.save(donation);
        });
    }
} 