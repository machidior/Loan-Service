package com.machidior.Loan_Management_Service.evaluator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialHistoryRequirementData {
    private Integer bankStatementMonths;
    private Integer mobileMoneyStatementMonths;
    private Boolean bankStatementProvided;
    private Boolean mobileMoneyStatementProvided;
}
