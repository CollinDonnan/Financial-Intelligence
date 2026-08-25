package com.example.be.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.be.Entities.TransactionEntity;
import com.example.be.Repos.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getTransactionById_returnsTransaction_whenFound() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setName("Groceries");
        transaction.setAmount(42.5);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionEntity result = transactionService.getTransactionById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Groceries");
        assertThat(result.getAmount()).isEqualTo(42.5);
        verify(transactionRepository).findById(1L);
    }

    @Test
    void getTransactionById_returnsNull_whenNotFound() {
        when(transactionRepository.findById(2L)).thenReturn(Optional.empty());

        TransactionEntity result = transactionService.getTransactionById(2L);

        assertThat(result).isNull();
    }
}
