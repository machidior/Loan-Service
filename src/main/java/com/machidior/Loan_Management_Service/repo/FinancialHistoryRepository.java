package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.FinancialHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialHistoryRepository extends JpaRepository<FinancialHistory, Long> {
}
