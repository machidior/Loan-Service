package com.machidior.Loan_Management_Service.completion;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.model.LoanApplication;

public interface RequirementCompletionService {

    void initializeRequirements(
            LoanApplication application,
            Requirements requirements
    );

    void markCompleted
            (String applicationNumber,
             RequirementType type);

    boolean areMandatoryRequirementsCompleted(
            String applicationNumber,
            Requirements requirements
    );
}


