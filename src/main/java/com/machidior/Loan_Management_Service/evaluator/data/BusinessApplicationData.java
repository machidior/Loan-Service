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
public class BusinessApplicationData {

    private Boolean registered;
    private Boolean businessLicenseProvided;
    private Boolean cashFlowStatementProvided;
    private Boolean tinCertificateProvided;
    private Boolean tinNumberProvided;
    private Boolean insuranceCoverProvided;

    private Integer yearsInOperation;
    private BigDecimal averageMonthlyTurnover;
}
