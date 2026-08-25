package com.example.be.Repos;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.be.Entities.Budget;
public interface BudgetRepository extends JpaRepository<Budget, Long> {

	Optional<Budget> findFirstByMonthOrderByIdDesc(String month);
}
