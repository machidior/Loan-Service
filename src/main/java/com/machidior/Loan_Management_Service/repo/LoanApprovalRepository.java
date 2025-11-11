package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanApprovalRepository extends JpaRepository<LoanApproval, Long> {
    //Optional<LoanApproval> findByApplicationNumber(String applicationNumber);
    Optional<LoanApproval> findByLoanId(String loanId);

}
