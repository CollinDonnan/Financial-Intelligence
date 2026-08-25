package com.example.be.Services;

import java.time.YearMonth;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.be.DTOs.BudgetDTO;
import com.example.be.Entities.Budget;
import com.example.be.Entities.TransactionEntity;
import com.example.be.Mappers.BudgetMapper;
import com.example.be.Repos.BudgetRepository;
import com.example.be.Repos.TransactionRepository;

@Service
public class BudgetService {

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Budget saveBudget(double amount) {
        BudgetDTO dto = new BudgetDTO(amount);
        dto.setMonth(YearMonth.now().toString());
        return budgetRepository.save(budgetMapper.toEntity(dto));
    }

    public Budget getCurrentMonthBudget() {
        String currentMonth = YearMonth.now().toString();
        return budgetRepository.findFirstByMonthOrderByIdDesc(currentMonth)
                .orElseGet(() -> saveBudget(0.0));
    }

    public Budget updateCurrentMonthBudget(double amount) {
        Budget budget = getCurrentMonthBudget();
        budget.setAmount(amount);
        return budgetRepository.save(budget);
    }

    public BudgetDTO getRemainingBudgetSummary(Long id) {
        Budget budget = budgetRepository.findById(id).orElse(null);
        if (budget == null) {
            BudgetDTO empty = new BudgetDTO();
            empty.setAmount(0.0);
            empty.setRemaining(0.0);
            return empty;
        }

        if (budget.getMonth() == null || budget.getMonth().isBlank()) {
            budget.setMonth(YearMonth.now().toString());
            budgetRepository.save(budget);
        }

        double spent = transactionRepository.findAll().stream()
            .filter(transaction -> transaction.getCreatedAt() != null)
            .filter(transaction -> YearMonth.from(transaction.getCreatedAt()).toString().equals(budget.getMonth()))
                .mapToDouble(TransactionEntity::getAmount)
                .sum();

        BudgetDTO summary = new BudgetDTO(budget.getAmount());
        summary.setId(budget.getId());
        summary.setMonth(budget.getMonth());
        summary.setRemaining(budget.getAmount() - spent);
        return summary;
    }
}
