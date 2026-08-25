package com.example.be.Services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.be.Entities.Budget;
import com.example.be.Mappers.BudgetMapper;
import com.example.be.Repos.BudgetRepository;
import com.example.be.Repos.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void saveBudget_setsCurrentMonth() {
        Budget budget = new Budget();
        budget.setAmount(1200.0);
        budget.setMonth(YearMonth.now().toString());

        when(budgetMapper.toEntity(any())).thenReturn(budget);
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.saveBudget(1200.0);

        assertThat(result).isNotNull();
        assertThat(result.getMonth()).isEqualTo(YearMonth.now().toString());
        assertThat(result.getAmount()).isEqualTo(1200.0);
    }
}
