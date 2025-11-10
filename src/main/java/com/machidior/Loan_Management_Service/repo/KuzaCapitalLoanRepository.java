package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.KuzaCapitalLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KuzaCapitalLoanRepository extends JpaRepository<KuzaCapitalLoan,String> {
    Optional<KuzaCapitalLoan> findByApplicationNumber(String applicationNumber);
}
