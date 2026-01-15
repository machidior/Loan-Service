package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.Collateral;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollateralRepository extends JpaRepository<Collateral, Long> {
    List<Collateral> findByLoanApplication(LoanApplication application);
}
