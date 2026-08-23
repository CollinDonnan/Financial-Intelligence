package com.example.be.Services;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.be.Entities.TransactionEntity;
import com.example.be.Repos.EntityRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TransactionService {

    @Autowired
    private EntityRepository entityRepository;

    public TransactionEntity saveTransaction(TransactionEntity transaction) {
        return entityRepository.save(transaction);
    }

    public TransactionEntity getTransactionById(Long id) {
        return entityRepository.findById(id).orElse(null);
    }

    public void deleteTransactionById(Long id) {
        entityRepository.deleteById(id);
    }

    public List<TransactionEntity> getAllTransactions() {
        return entityRepository.findAll();
    }
    
}
