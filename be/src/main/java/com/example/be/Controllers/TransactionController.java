package com.example.be.Controllers; 
import com.example.be.DTOs.TransactionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.be.Services.TransactionService;
import com.example.be.Mappers.TransactionMapper;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMapper transactionMapper;
    
    @GetMapping("/transaction/{id}")
    public TransactionDTO transactions(@PathVariable("id") int id) {
        return transactionMapper.toDTO(transactionService.getTransactionById((long) id));
    }

    @GetMapping("/transactions")
    public List<TransactionDTO> allTransactions() {
        return transactionService.getAllTransactions().stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/transactions")
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transaction) {
        return transactionMapper.toDTO(transactionService.saveTransaction(transactionMapper.toEntity(transaction)));
    }

    @DeleteMapping("/transaction/{id}")
    public void deleteTransaction(@PathVariable("id") int id) {
        transactionService.deleteTransactionById((long) id);
    }
}
