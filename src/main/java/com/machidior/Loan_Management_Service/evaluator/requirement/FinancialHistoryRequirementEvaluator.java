package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.FinancialHistoryRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.FinancialHistoryRequirement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FinancialHistoryRequirementEvaluator
        implements RequirementEvaluator<
                FinancialHistoryRequirement,
                FinancialHistoryRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.FINANCIAL_HISTORY;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            FinancialHistoryRequirementData data,
            FinancialHistoryRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("financialHistory", "Financial history is required"));
            }
            return result(violations);
        }

        check(cfg.getBankStatementRequired(),
                data.getBankStatementProvided(),
                "bankStatement",
                violations);

        check(cfg.getMobileMoneyStatementRequired(),
                data.getMobileMoneyStatementProvided(),
                "mobileMoneyStatement",
                violations);

//        if (cfg.getMinCreditScore() != null &&
//                (data.getCreditScore() == null ||
//                        data.getCreditScore() < cfg.getMinCreditScore())) {
//
//            violations.add(v(
//                    "creditScore",
//                    "Minimum credit score is " + cfg.getMinCreditScore()
//            ));
//        }

        if (cfg.getBankStatementMonths() != null &&
                (data.getBankStatementMonths() == null ||
                        data.getBankStatementMonths() < cfg.getBankStatementMonths())) {

            violations.add(v(
                    "bankStatementMonths",
                    "Minimum bank statement months is " + cfg.getBankStatementMonths()
            ));
        }

        if (cfg.getMobileMoneyStatementMonths() != null &&
                (data.getMobileMoneyStatementMonths() == null ||
                        data.getMobileMoneyStatementMonths() < cfg.getMobileMoneyStatementMonths())) {

            violations.add(v(
                    "mobileMoneyStatementMonths",
                    "Minimum mobile money statement months is " + cfg.getMobileMoneyStatementMonths()
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

