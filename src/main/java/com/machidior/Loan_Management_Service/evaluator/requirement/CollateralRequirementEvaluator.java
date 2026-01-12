package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.CollateralItemData;
import com.machidior.Loan_Management_Service.evaluator.data.CollateralRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.CollateralRequirement;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CollateralRequirementEvaluator implements
        RequirementEvaluator<
                        CollateralRequirement,
                CollateralRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.COLLATERAL;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            CollateralRequirementData data,
            CollateralRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null || data.getCollateralItems() == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("collaterals", "Collateral details are required"));
            }
            return result(violations);
        }

        List<CollateralItemData> items = data.getCollateralItems();

        if (cfg.getMinCount() != null && items.size() < cfg.getMinCount()) {
            violations.add(v(
                    "collaterals",
                    "Minimum collateral count is " + cfg.getMinCount()
            ));
        }

        if (cfg.getMaxCount() != null && items.size() > cfg.getMaxCount()) {
            violations.add(v(
                    "collaterals",
                    "Maximum collateral count is " + cfg.getMaxCount()
            ));
        }

        if (cfg.getAllowedTypes() != null && !cfg.getAllowedTypes().isEmpty()) {
            for (CollateralItemData item : items) {
                if (!cfg.getAllowedTypes().contains(item.getType())) {
                    violations.add(v(
                            "type",
                            "Collateral type " + item.getType() + " is not allowed"
                    ));
                }
            }
        }

        if (cfg.getMinLoanAmountToValueRatio() != null ) {

            if (data.getTotalCollateralValue().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal ratio = data.getTotalCollateralValue()
                        .divide(data.getRequestedAmount());

                if (ratio.compareTo(cfg.getMinLoanAmountToValueRatio()) < 0) {
                    violations.add(v(
                            "collaterals",
                            "The collateral value to requested amount ratio is below the minimum of "
                                    + cfg.getMinLoanAmountToValueRatio()
                    ));
                }
            } else {
                violations.add(v(
                        "collaterals",
                        "Total collateral value must be greater than zero to evaluate value to requested amount ratio"
                ));
            }
        }

        for (int i = 0; i < items.size(); i++) {
            CollateralItemData item = items.get(i);
            validateItem(cfg, item, i, violations);        }


        return result(violations);
    }

    private void validateItem(
            CollateralRequirement cfg,
            CollateralItemData item,
            int index,
            List<RequirementViolation> violations
    ) {

        String prefix = "collaterals[" + index + "]";

        check(cfg.getDescriptionRequired(),
                item.getDescriptionProvided(),
                prefix + ".description",
                violations);

        check(cfg.getOwnershipProofRequired(),
                item.getOwnershipProofProvided(),
                prefix + ".ownershipProof",
                violations);

        check(cfg.getInsuranceRequired(),
                item.getInsuranceProvided(),
                prefix + ".insurance",
                violations);

        check(cfg.getValuationRequired(),
                item.getValuationProvided(),
                prefix + ".valuation",
                violations);

        check(cfg.getPhotoRequired(),
                item.getPhotoProvided(),
                prefix + ".photo",
                violations);
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

