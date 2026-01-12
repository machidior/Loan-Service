package com.machidior.Loan_Management_Service.evaluator;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequirementEvaluationResult {

    private RequirementType type;
    private boolean passed;
    private List<RequirementViolation> violations = new ArrayList<>();

    public static RequirementEvaluationResult passed(RequirementType type) {
        RequirementEvaluationResult r = new RequirementEvaluationResult();
        r.type = type;
        r.passed = true;
        return r;
    }

    public static RequirementEvaluationResult failed(
            RequirementType type,
            List<RequirementViolation> violations
    ) {
        RequirementEvaluationResult r = new RequirementEvaluationResult();
        r.type = type;
        r.passed = false;
        r.violations = violations;
        return r;
    }
}
