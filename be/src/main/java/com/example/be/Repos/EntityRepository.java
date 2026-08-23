package com.example.be.Repos;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.be.Entities.TransactionEntity;

public interface EntityRepository extends JpaRepository<TransactionEntity, Long> {
    
}
