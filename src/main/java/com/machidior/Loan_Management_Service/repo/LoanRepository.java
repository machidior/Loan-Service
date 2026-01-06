package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, String> {
    Optional<Loan> findByApplicationNumber(String applicationNumber);
    List<Loan> findByCustomerId(String customerId);
}
