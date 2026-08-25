package com.example.be.Mappers;

import java.time.YearMonth;

import org.springframework.stereotype.Component;

import com.example.be.DTOs.BudgetDTO;
import com.example.be.Entities.Budget;

@Component
public class BudgetMapper {
    public BudgetDTO toDTO(double amount) {
        BudgetDTO budget = new BudgetDTO();
        budget.setAmount(amount);
        budget.setMonth(YearMonth.now().toString());
        return budget;
    }

    public Budget toEntity(BudgetDTO budget) {
        Budget entity = new Budget();
        entity.setAmount(budget.getAmount());
        entity.setMonth(budget.getMonth() != null ? budget.getMonth() : YearMonth.now().toString());
        return entity;
    }
}

