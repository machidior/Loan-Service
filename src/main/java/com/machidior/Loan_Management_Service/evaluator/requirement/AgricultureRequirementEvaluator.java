package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.AgricultureRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.AgricultureRequirement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgricultureRequirementEvaluator
        implements RequirementEvaluator<
                AgricultureRequirement,
        AgricultureRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.AGRICULTURE;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            AgricultureRequirementData data,
            AgricultureRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("agriculture", "Agriculture data is required"));
            }
            return result(violations);
        }

        // Min farm size
        if (cfg.getMinFarmSize() != null) {
            if (data.getFarmSize() == null ||
                    data.getFarmSize().intValue() < cfg.getMinFarmSize()) {

                violations.add(
                        v("farmSize",
                                "Minimum farm size is " + cfg.getMinFarmSize())
                );
            }
        }

        check(cfg.getFarmDetailsRequired(),
                data.getFarmDetailsProvided(),
                "farmDetails",
                violations);

        check(cfg.getProductionCycleRequired(),
                data.getProductionCycleProvided(),
                "productionCycle",
                violations);

        check(cfg.getOffTakerAgreement(),
                data.getOffTakerAgreementProvided(),
                "offTakerAgreement",
                violations);

        check(cfg.getFarmInspectionRequired(),
                data.getFarmInspectionCompleted(),
                "farmInspection",
                violations);

        return result(violations);
    }

    private void check(
            Boolean required,
            Boolean provided,
            String field,
            List<RequirementViolation> violations
    ) {
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
