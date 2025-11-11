package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanTermination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanTerminationRepository extends JpaRepository<LoanTermination, Long> {
    //Optional<LoanTermination> findByLoanApplicationNumber(String loanApplicationNumber);
    Optional<LoanTermination> findByLoanId(String loanId);
}
