package com.example.be.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RestController;

import com.example.be.DTOs.BudgetDTO;
import com.example.be.Entities.Budget;
import com.example.be.Services.BudgetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Budget", description = "Endpoints budget management")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Operation(summary = "Get the current month's budget", description = "Returns the budget for the current month along with the remaining amount")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Budget retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Budget not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/budget")
    public BudgetDTO getBudget() {
        Budget currentMonthBudget = budgetService.getCurrentMonthBudget();
        return budgetService.getRemainingBudgetSummary(currentMonthBudget.getId());
    }

    @Operation(summary = "Get the remaining budget for the current month", description = "Returns the remaining budget for the current month")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Remaining budget retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Budget not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/budget/remaining")
    public BudgetDTO getRemainingBudget() {
        Budget currentMonthBudget = budgetService.getCurrentMonthBudget();
        return budgetService.getRemainingBudgetSummary(currentMonthBudget.getId());
    }

    @Operation(summary = "Set the budget for the current month", description = "Updates the budget for the current month with the provided amount")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Budget updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid budget data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/budget")
    public BudgetDTO setBudget(@RequestBody BudgetDTO budget) {
        Budget savedBudget = budgetService.updateCurrentMonthBudget(budget.getAmount());
        return budgetService.getRemainingBudgetSummary(savedBudget.getId());
    }
}
