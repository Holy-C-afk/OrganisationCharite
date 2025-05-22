package com.charite.repository;

import com.charite.model.CharityAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CharityActionRepository extends JpaRepository<CharityAction, Long> {
    List<CharityAction> findByOrganizerId(Long organizerId);
    List<CharityAction> findByCategory(CharityAction.ActionCategory category);
    List<CharityAction> findByStatus(CharityAction.ActionStatus status);
} 