package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.BusinessLoanCollateral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessLoanCollateralRepository extends JpaRepository<BusinessLoanCollateral, Long> {
    List<BusinessLoanCollateral> findByBusinessLoanId(String loanId);
}
