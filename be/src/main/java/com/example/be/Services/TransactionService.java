package com.example.be.Services;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.be.Entities.TransactionEntity;
import com.example.be.Repos.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionEntity saveTransaction(TransactionEntity transaction) {
        return transactionRepository.save(transaction);
    }

    public TransactionEntity getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public TransactionEntity updateTransaction(Long id, TransactionEntity transaction) {
        TransactionEntity existing = transactionRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setName(transaction.getName());
        existing.setAmount(transaction.getAmount());
        return transactionRepository.save(existing);
    }

    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
}
