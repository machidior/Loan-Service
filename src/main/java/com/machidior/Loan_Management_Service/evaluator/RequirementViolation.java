package com.machidior.Loan_Management_Service.evaluator;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequirementViolation {

    private RequirementType type;
    private String field;
    private String message;
}

