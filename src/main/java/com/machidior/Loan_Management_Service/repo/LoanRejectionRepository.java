package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanRejection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRejectionRepository extends JpaRepository<LoanRejection, Long> {
   // Optional<LoanRejection> findByApplicationNumber(String applicationNumber);
    Optional<LoanRejection> findByLoanId(String loanId);
}
