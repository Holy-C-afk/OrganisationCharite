package com.charite.repository;

import com.charite.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUserId(Long userId);
    List<Donation> findByCategory(Donation.DonationCategory category);
    List<Donation> findByCharityActionId(Long charityActionId);
    List<Donation> findByStatus(Donation.Status status);
} 