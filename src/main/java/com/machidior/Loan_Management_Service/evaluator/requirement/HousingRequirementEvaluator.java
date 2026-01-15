package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.HousingRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.HousingRequirement;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class HousingRequirementEvaluator
        implements RequirementEvaluator<
                HousingRequirement,
                HousingRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.HOUSING;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            HousingRequirementData data,
            HousingRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("housing", "Housing data is required"));
            }
            return result(violations);
        }

        check(cfg.getOwnershipProofRequired(),
                data.getOwnershipProofProvided(),
                "ownershipProof",
                violations);

        check(cfg.getValuationRequired(),
                data.getValuationProvided(),
                "valuation",
                violations);

        check(cfg.getBuildingPlanRequired(),
                data.getBuildingPlanProvided(),
                "buildingPlan",
                violations);

        check(cfg.getSiteInspectionRequired(),
                data.getSiteInspectionReportProvided(),
                "siteInspectionReport",
                violations);

        if (cfg.getMinValueToLoanRatio() != null) {
            if (data.getValueToLoanRatio().compareTo(cfg.getMinValueToLoanRatio()) < 0) {
                violations.add(v("valueToLoanRatio",
                        "Value to loan ratio is below the minimum required of "
                                + cfg.getMinValueToLoanRatio()));
            }
        }

        return result(violations);
    }

    private void check(Boolean required, Boolean provided,
                       String field, List<RequirementViolation> violations) {

        if (Boolean.TRUE.equals(required) &&
                !Boolean.TRUE.equals(provided)) {
            violations.add(v(field, field + " is required"));
        }
    }

    private RequirementViolation v(String field, String msg) {
        RequirementViolation v = new RequirementViolation();
        v.setType(supports());
        v.setField(field);
        v.setMessage(msg);
        return v;
    }

    private RequirementEvaluationResult result(List<RequirementViolation> v) {
        return v.isEmpty()
                ? RequirementEvaluationResult.passed(supports())
                : RequirementEvaluationResult.failed(supports(), v);
    }
}

