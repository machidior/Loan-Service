package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.Guarantor;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuarantorRepository extends JpaRepository<Guarantor,Long> {
    Optional<Guarantor> findByLoanApplication(LoanApplication application);
}
