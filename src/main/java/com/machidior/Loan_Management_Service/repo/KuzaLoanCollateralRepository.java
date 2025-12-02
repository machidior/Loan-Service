package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.KuzaLoanCollateral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KuzaLoanCollateralRepository extends JpaRepository<KuzaLoanCollateral, Long> {
}
