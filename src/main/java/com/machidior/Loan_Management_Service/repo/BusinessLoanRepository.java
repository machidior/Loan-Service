package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.BusinessLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessLoanRepository extends JpaRepository<BusinessLoan, String> {
    Optional<BusinessLoan> findByApplicationNumber(String applicationNumber);
}
