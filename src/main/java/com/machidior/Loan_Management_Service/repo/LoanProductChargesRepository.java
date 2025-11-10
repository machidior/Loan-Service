package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.model.LoanProductCharges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanProductChargesRepository extends JpaRepository<LoanProductCharges, Long> {
    Optional<LoanProductCharges> findByProductType(LoanProductType type);
}
