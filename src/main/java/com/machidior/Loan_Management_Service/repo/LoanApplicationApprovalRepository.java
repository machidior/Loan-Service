package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanApplicationApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanApplicationApprovalRepository extends JpaRepository<LoanApplicationApproval, Long> {
    Optional<LoanApplicationApproval> findByApplicationNumber(String applicationNumber);

}
