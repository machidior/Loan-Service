package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.GuarantorItemData;
import com.machidior.Loan_Management_Service.evaluator.data.GuarantorRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.GuarantorRequirement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GuarantorRequirementEvaluator
        implements RequirementEvaluator<GuarantorRequirement, GuarantorRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.GUARANTOR;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            GuarantorRequirementData data,
            GuarantorRequirement requirement
    ) {

        List<RequirementViolation> violations = new ArrayList<>();

        if (Boolean.FALSE.equals(requirement.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        int count = data.getGuarantors().size();

        if (requirement.getMinGuarantors() != null &&
                count < requirement.getMinGuarantors()) {

            violations.add(v(
                    "guarantors",
                    "At least " + requirement.getMinGuarantors() +
                            " guarantors are required"
                    )

            );
        }

        if (requirement.getMaxGuarantors() != null &&
                count > requirement.getMaxGuarantors()) {

            violations.add(v(
                    "guarantors",
                    "At most " + requirement.getMaxGuarantors() +
                            " guarantors are allowed"
                    )
            );
        }

        for (int i = 0; i < data.getGuarantors().size(); i++) {

            GuarantorItemData item = data.getGuarantors().get(i);
            int index = i + 1;

            if (requirement.getMinGuarantorIncome() != null) {
                if (item.getIncome() == null ||
                        item.getIncome()
                                .compareTo(requirement.getMinGuarantorIncome()) < 0) {

                    violations.add(v(
                            "guarantor_" + index + "_income",
                            "Guarantor " + index +
                                    " income must be at least " +
                                    requirement.getMinGuarantorIncome()
                            )
                    );
                }
            }

            if (Boolean.TRUE.equals(requirement.getGuarantorEmploymentRequired()) &&
                    Boolean.FALSE.equals(item.getEmploymentProvided())) {

                violations.add(v(
                        "guarantor_" + index + "_employment",
                        "Guarantor " + index +
                                " employment details are required"
                        )
                );
            }

            if (Boolean.TRUE.equals(requirement.getGuarantorRelationRequired()) &&
                    Boolean.FALSE.equals(item.getRelationProvided())) {

                violations.add(v(
                        "guarantor_" + index + "_relation",
                        "Guarantor " + index +
                                " relationship details are required"
                        )

                );
            }

            if (Boolean.TRUE.equals(requirement.getGuarantorIncomeProofRequired()) &&
                    Boolean.FALSE.equals(item.getIncomeProofProvided())) {

                violations.add(v(
                        "guarantor_" + index + "_income_proof",
                        "Guarantor " + index +
                                " income proof is required"
                        )

                );
            }

            if (Boolean.TRUE.equals(requirement.getPassportPhotoRequired()) &&
            Boolean.FALSE.equals(item.getPassportPhotoProvided())) {

                violations.add(v(
                        "guarantor_" + index + "_passport_photo",
                        "Guarantor " + index +
                                " passport photo is required"
                        )

                );
            }

            if (Boolean.TRUE.equals(requirement.getIdDocumentRequired()) &&
            Boolean.FALSE.equals(item.getIdDocumentProvided())) {
                violations.add(v(
                        "guarantor_" + index + "_id_document",
                        "Guarantor " + index +
                                " ID document is required"
                        )

                );
            }

            if (Boolean.TRUE.equals(requirement.getGuarantorConsentRequired()) &&
            Boolean.FALSE.equals(item.getGuarantorConsentProvided())) {
                violations.add(v(
                        "guarantor_" + index + "_consent",
                        "Guarantor " + index +
                                " consent is required"
                        )

                );
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
