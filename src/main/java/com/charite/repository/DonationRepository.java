package com.charite.repository;

import com.charite.model.Donation;
import com.charite.model.User;
import com.charite.model.CharityAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUser(User user);
    List<Donation> findByCharityAction(CharityAction charityAction);
    List<Donation> findByCategory(Donation.DonationCategory category);
    List<Donation> findByStatus(Donation.Status status);
} 