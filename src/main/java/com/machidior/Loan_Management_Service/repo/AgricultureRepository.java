package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.AgricultureRequirementDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgricultureRepository extends JpaRepository<AgricultureRequirementDetails, Long> {
}
