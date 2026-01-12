package com.machidior.Loan_Management_Service.evaluator;

import com.machidior.Loan_Management_Service.enums.RequirementType;

public interface RequirementEvaluator<C, D> {

    RequirementType supports();

    RequirementEvaluationResult evaluate(
            D requirementData,
            C config
    );
}

