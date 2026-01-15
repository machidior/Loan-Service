package com.machidior.Loan_Management_Service.completion;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanApplicationRequirementStatusRepository
        extends JpaRepository<LoanApplicationRequirementStatus, Long> {

    Optional<LoanApplicationRequirementStatus>
    findByApplicationNumberAndRequirementType(
            String applicationNumber,
            RequirementType requirementType
    );

    List<LoanApplicationRequirementStatus>
    findAllByApplicationNumber(String applicationNumber);
}

