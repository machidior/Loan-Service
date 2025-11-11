package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanClosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanClosureRepository extends JpaRepository<LoanClosure, Long> {
   // Optional<LoanClosure> findByLoanApplicationNumber(String loanApplicationNumber);
    Optional<LoanClosure> findByLoanId(String loanId);
}
