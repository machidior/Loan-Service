package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.BusinessDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessDetailsRepository extends JpaRepository<BusinessDetails, Long> {
}
