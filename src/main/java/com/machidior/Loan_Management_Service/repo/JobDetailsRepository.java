package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.EmploymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDetailsRepository extends JpaRepository<EmploymentDetails, Long> {
}
