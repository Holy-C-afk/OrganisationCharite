package com.charite.repository;

import com.charite.model.CharityAction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CharityActionRepository extends MongoRepository<CharityAction, String> {
    List<CharityAction> findByOrganizationId(String organizationId);
    List<CharityAction> findByStatus(String status);
    List<CharityAction> findByCategory(String category);
} 