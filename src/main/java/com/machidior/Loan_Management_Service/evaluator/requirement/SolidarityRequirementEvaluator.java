package com.machidior.Loan_Management_Service.evaluator.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import com.machidior.Loan_Management_Service.evaluator.data.SolidarityRequirementData;
import com.machidior.Loan_Management_Service.feign.requirement.SolidarityRequirement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SolidarityRequirementEvaluator
        implements RequirementEvaluator<
                SolidarityRequirement,
                SolidarityRequirementData> {

    @Override
    public RequirementType supports() {
        return RequirementType.SOLIDARITY;
    }

    @Override
    public RequirementEvaluationResult evaluate(
            SolidarityRequirementData data,
            SolidarityRequirement cfg
    ) {

        if (!Boolean.TRUE.equals(cfg.getEnabled())) {
            return RequirementEvaluationResult.passed(supports());
        }

        List<RequirementViolation> violations = new ArrayList<>();

        if (data == null) {
            if (Boolean.TRUE.equals(cfg.getMandatory())) {
                violations.add(v("solidarity", "Group data is required"));
            }
            return result(violations);
        }

        if (cfg.getMinGroupMembers() != null &&
                data.getItems().size() < cfg.getMinGroupMembers()) {

            violations.add(v(
                    "memberCount",
                    "Minimum group members is " + cfg.getMinGroupMembers()
            ));
        }

        if (cfg.getMaxGroupMembers() != null &&
                data.getItems().size() > cfg.getMaxGroupMembers()) {

            violations.add(v(
                    "memberCount",
                    "Maximum group members is " + cfg.getMaxGroupMembers()
            ));
        }

        for (int i = 0; i < data.getItems().size(); i++) {
            var member = data.getItems().get(i);

            if (Boolean.TRUE.equals(cfg.getGroupRegistrationRequired()) &&
                    !Boolean.TRUE.equals(member.getGroupRegistrationProvided())) {

                violations.add(v(
                        "members[" + i + "].groupRegistrationProvided",
                        "Group registration document is required"
                ));
            }

            if (Boolean.TRUE.equals(cfg.getGroupGuaranteeRequired()) &&
                    !Boolean.TRUE.equals(member.getGroupGuaranteeProvided())) {

                violations.add(v(
                        "members[" + i + "].groupGuaranteeProvided",
                        "Group guarantee document is required"
                ));
            }

            if (Boolean.TRUE.equals(cfg.getGroupMeetingRecordsRequired()) &&
                    !Boolean.TRUE.equals(member.getGroupMeetingRecordsProvided())) {

                violations.add(v(
                        "members[" + i + "].groupMeetingRecordsProvided",
                        "Group meeting records are required"
                ));
            }
        }

        return result(violations);
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

