package com.example.be.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.be.DTOs.BudgetDTO;
import com.example.be.Entities.Budget;
import com.example.be.Services.BudgetService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Budget", description = "Endpoints budget management")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/budget")
    public BudgetDTO getBudget() {
        Budget currentMonthBudget = budgetService.getCurrentMonthBudget();
        return budgetService.getRemainingBudgetSummary(currentMonthBudget.getId());
    }

    @GetMapping("/budget/remaining")
    public BudgetDTO getRemainingBudget() {
        Budget currentMonthBudget = budgetService.getCurrentMonthBudget();
        return budgetService.getRemainingBudgetSummary(currentMonthBudget.getId());
    }

    @PostMapping("/budget")
    public BudgetDTO setBudget(@RequestBody BudgetDTO budget) {
        Budget savedBudget = budgetService.updateCurrentMonthBudget(budget.getAmount());
        return budgetService.getRemainingBudgetSummary(savedBudget.getId());
    }
}
