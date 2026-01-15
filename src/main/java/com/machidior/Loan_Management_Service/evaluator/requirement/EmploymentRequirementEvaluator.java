package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.EmploymentRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.EmploymentRequirement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmploymentRequirementEvaluator
        implements RequirementEvaluator<
        EmploymentRequirement,
        EmploymentRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.EMPLOYMENT_DETAILS;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            EmploymentRequirementData data,
            EmploymentRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("employment", "Employment data is required"));
            }
            return result(violations);
        }

        check(cfg.getJobContractRequired(),
                data.getJobContractProvided(),
                "jobContract",
                violations);

        check(cfg.getPaySlipRequired(),
                data.getPaySlipProvided(),
                "paySlip",
                violations);

        check(cfg.getPayrollDeductionRequired(),
                data.getPayrollDeductionProvided(),
                "payrollDeduction",
                violations);

        if (cfg.getMinMonthsEmployed() != null &&
                (data.getMonthsEmployed() == null ||
                        data.getMonthsEmployed() < cfg.getMinMonthsEmployed())) {

            violations.add(v(
                    "monthsEmployed",
                    "Minimum employment duration is " + cfg.getMinMonthsEmployed()
            ));
        }

        if (cfg.getMinNetSalary() != null &&
                (data.getNetMonthlyIncome() == null ||
                        data.getNetMonthlyIncome().compareTo(cfg.getMinNetSalary()) < 0)) {

            violations.add(v(
                    "netSalary",
                    "Minimum net salary is " + cfg.getMinNetSalary()
            ));
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
