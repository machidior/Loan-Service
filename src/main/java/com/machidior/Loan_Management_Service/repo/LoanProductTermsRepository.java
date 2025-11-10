package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.model.LoanProductTerms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanProductTermsRepository extends JpaRepository<LoanProductTerms, Long> {
    Optional<LoanProductTerms> findByProductType(LoanProductType type);
}
