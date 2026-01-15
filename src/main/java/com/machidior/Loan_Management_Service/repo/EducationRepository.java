package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.Education;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education, Long> {
}
