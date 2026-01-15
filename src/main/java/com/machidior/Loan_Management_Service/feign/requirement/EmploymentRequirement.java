package com.machidior.Loan_Management_Service.feign.requirement;

import com.machidior.Loan_Management_Service.completion.ProductRequirementConfig;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmploymentRequirement implements ProductRequirementConfig {

    private Long id;
    private RequirementType type;
    private Boolean enabled;
    private Boolean mandatory;

    private Boolean jobContractRequired;
    private Boolean paySlipRequired;
    private Boolean payrollDeductionRequired;

    private Integer minMonthsEmployed;

    private BigDecimal minNetSalary;
}
