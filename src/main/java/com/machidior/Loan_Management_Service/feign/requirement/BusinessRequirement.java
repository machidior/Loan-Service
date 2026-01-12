package com.machidior.Loan_Management_Service.feign.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessRequirement {

    private Long id;
    private RequirementType type;
    private Boolean enabled;
    private Boolean mandatory;

    private Boolean registrationRequired;
    private Boolean businessLicenseRequired;
    private Boolean cashFlowStatementRequired;
    private Boolean tinCertificateRequired;
    private Boolean tinNumberRequired;
    private Boolean insuranceComprehensiveCoverRequired;


    private Integer minYearsInOperation;

    private BigDecimal minAverageMonthlyTurnOver;
}
