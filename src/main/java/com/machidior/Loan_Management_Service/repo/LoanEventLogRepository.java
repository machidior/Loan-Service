package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanEventLogRepository extends JpaRepository<LoanEventLog, Long> {
    List<LoanEventLog> findByLoanApplicationIdOrderByCreatedAtDesc(Long loanApplicationId);
}
