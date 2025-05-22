package com.charite.service;

import com.charite.model.Donation;
import com.charite.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Donation> getDonationById(String id) {
        return donationRepository.findById(id);
    }

    public List<Donation> getDonationsByUser(String userId) {
        return donationRepository.findByUserId(userId);
    }

    public List<Donation> getDonationsByCharityAction(String charityActionId) {
        return donationRepository.findByCharityActionId(charityActionId);
    }

    public List<Donation> getDonationsByStatus(String status) {
        return donationRepository.findByStatus(status);
    }

    public Donation createDonation(Donation donation) {
        donation.setCreatedAt(LocalDateTime.now());
        donation.setUpdatedAt(LocalDateTime.now());
        return donationRepository.save(donation);
    }

    public Optional<Donation> updateDonation(String id, Donation donation) {
        return donationRepository.findById(id)
                .map(existingDonation -> {
                    existingDonation.setAmount(donation.getAmount());
                    existingDonation.setPaymentMethod(donation.getPaymentMethod());
                    existingDonation.setStatus(donation.getStatus());
                    existingDonation.setTransactionId(donation.getTransactionId());
                    existingDonation.setMessage(donation.getMessage());
                    existingDonation.setAnonymous(donation.isAnonymous());
                    existingDonation.setUpdatedAt(LocalDateTime.now());
                    return donationRepository.save(existingDonation);
                });
    }

    public boolean deleteDonation(String id) {
        if (donationRepository.existsById(id)) {
            donationRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 