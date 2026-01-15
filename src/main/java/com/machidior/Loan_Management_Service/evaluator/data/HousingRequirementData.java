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
public class HousingRequirementData {

    private Boolean ownershipProofProvided;
    private Boolean valuationProvided;
    private Boolean buildingPlanProvided;
    private Boolean siteInspectionReportProvided;
    private BigDecimal ValueToLoanRatio;
}
