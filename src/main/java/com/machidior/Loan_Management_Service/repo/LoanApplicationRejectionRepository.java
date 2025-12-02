package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanApplicationRejection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanApplicationRejectionRepository extends JpaRepository<LoanApplicationRejection, Long> {
   Optional<LoanApplicationRejection> findByApplicationNumber(String applicationNumber);
}
