package com.machidior.Loan_Management_Service.evaluator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentRequirementData {

    private Boolean jobContractProvided;
    private Boolean paySlipProvided;
    private Integer monthsEmployed;
    private Boolean payrollDeductionProvided;
    private BigDecimal netMonthlyIncome;
}
