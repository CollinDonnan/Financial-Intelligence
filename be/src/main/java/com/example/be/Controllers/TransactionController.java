package com.example.be.Controllers; 
import com.example.be.DTOs.TransactionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.be.Services.TransactionService;
import com.example.be.Mappers.TransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Transactions", description = "Endpoints for managing financial transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMapper transactionMapper;
    
    @Operation(summary = "Get a transaction by id", description = "Returns a single transaction matching the given id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction found"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/transaction/{id}")
    public TransactionDTO transactions(@Parameter(description = "Id of the transaction to retrieve") @PathVariable("id") int id) {
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

    @PutMapping("/transaction/{id}")
    public TransactionDTO updateTransaction(@PathVariable("id") int id, @RequestBody TransactionDTO transaction) {
        return transactionMapper.toDTO(transactionService.updateTransaction((long) id, transactionMapper.toEntity(transaction)));
    }

    @DeleteMapping("/transaction/{id}")
    public void deleteTransaction(@PathVariable("id") int id) {
        transactionService.deleteTransactionById((long) id);
    }
}
