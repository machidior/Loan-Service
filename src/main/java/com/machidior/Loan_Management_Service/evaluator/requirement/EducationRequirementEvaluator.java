package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.EducationRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.EducationRequirement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EducationRequirementEvaluator
        implements RequirementEvaluator<
                EducationRequirement,
                EducationRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.EDUCATION;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            EducationRequirementData data,
            EducationRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("education", "Education data is required"));
            }
            return result(violations);
        }

        check(cfg.getAdmissionLetterRequired(),
                data.getAdmissionLetterProvided(),
                "admissionLetter",
                violations);

        check(cfg.getFeeStructureRequired(),
                data.getFeeStructureProvided(),
                "feeStructure",
                violations);

        check(cfg.getSponsorRequired(),
                data.getSponsorProvided(),
                "sponsor",
                violations);

        check(cfg.getGuarantorRequired(),
                data.getGuarantorProvided(),
                "guarantor",
                violations);

        check(cfg.getRelatedEducationCertificateRequired(),
                data.getRelatedEducationCertificateProvided(),
                "relatedEducationCertificate",
                violations);

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
