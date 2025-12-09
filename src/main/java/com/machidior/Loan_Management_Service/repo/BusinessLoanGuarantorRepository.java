package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.BusinessLoanApplication;
import com.machidior.Loan_Management_Service.model.BusinessLoanGuarantor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BusinessLoanGuarantorRepository extends JpaRepository<BusinessLoanGuarantor, Long> {
    Optional<BusinessLoanGuarantor> findByBusinessLoanApplication(BusinessLoanApplication businessLoanApplication);
}
