package com.machidior.Loan_Management_Service.dtos.response;

import com.machidior.Loan_Management_Service.evaluator.RequirementViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequirementSubmissionResponse {

    private boolean success;
    private List<RequirementViolation> violations;
}
