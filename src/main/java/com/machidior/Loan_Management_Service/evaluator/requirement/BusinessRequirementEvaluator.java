package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.BusinessApplicationData;
import com.machidior.Loan_Management_Service.feign.requirement.BusinessRequirement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessRequirementEvaluator
        implements RequirementEvaluator<
                BusinessRequirement,
                BusinessApplicationData> {

    @Override
    public RequirementType supports() {
        return RequirementType.BUSINESS_DETAILS;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            BusinessApplicationData data,
            BusinessRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("business", "Business details are required"));
            }
            return result(violations);
        }

        check(cfg.getRegistrationRequired(),
                data.getRegistered(),
                "registration",
                violations);

        check(cfg.getBusinessLicenseRequired(),
                data.getBusinessLicenseProvided(),
                "businessLicense",
                violations);

        check(cfg.getCashFlowStatementRequired(),
                data.getCashFlowStatementProvided(),
                "cashFlowStatement",
                violations);

        check(cfg.getTinCertificateRequired(),
                data.getTinCertificateProvided(),
                "tinCertificate",
                violations);

        check(cfg.getTinNumberRequired(),
                data.getTinNumberProvided(),
                "tinNumber",
                violations);

        check(cfg.getInsuranceComprehensiveCoverRequired(),
                data.getInsuranceCoverProvided(),
                "insuranceCover",
                violations);

        if (cfg.getMinYearsInOperation() != null) {
            if (data.getYearsInOperation() == null ||
                    data.getYearsInOperation() < cfg.getMinYearsInOperation()) {

                violations.add(v(
                        "yearsInOperation",
                        "Minimum years in operation is " + cfg.getMinYearsInOperation()
                ));
            }
        }

        if (cfg.getMinAverageMonthlyTurnOver() != null) {
            if (data.getAverageMonthlyTurnover() == null ||
                    data.getAverageMonthlyTurnover()
                            .compareTo(cfg.getMinAverageMonthlyTurnOver()) < 0) {

                violations.add(v(
                        "averageMonthlyTurnover",
                        "Minimum average monthly turnover is " +
                                cfg.getMinAverageMonthlyTurnOver()
                ));
            }
        }

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
