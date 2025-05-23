package com.charite.repository;

import com.charite.model.CharityAction;
import com.charite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CharityActionRepository extends JpaRepository<CharityAction, Long> {
    List<CharityAction> findByStatus(CharityAction.ActionStatus status);
    List<CharityAction> findByOrganizer(User organizer);
} 