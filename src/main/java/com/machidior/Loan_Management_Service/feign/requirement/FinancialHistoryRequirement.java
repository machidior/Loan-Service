package com.machidior.Loan_Management_Service.feign.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialHistoryRequirement {

    private Long id;
    private RequirementType type;
    private Boolean enabled;
    private Boolean mandatory;

    private Boolean bankStatementRequired;
    private Boolean creditHistoryCheckRequired;
    private Boolean mobileMoneyStatementRequired;

    private Integer bankStatementMonths;
    private Integer mobileMoneyStatementMonths;
    private Integer minCreditScore;
}
