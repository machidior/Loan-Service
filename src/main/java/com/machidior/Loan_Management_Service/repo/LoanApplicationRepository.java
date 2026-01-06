package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, String> {
    List<LoanApplication> findByCustomerIdAndProductId(String customerId, Long productId);
}
