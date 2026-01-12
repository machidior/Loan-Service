package com.machidior.Loan_Management_Service.feign.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HousingRequirement {

    private Long id;
    private RequirementType type;
    private Boolean enabled;
    private Boolean mandatory;

    private Boolean ownershipProofRequired;
    private Boolean buildingPlanRequired;
    private Boolean siteInspectionRequired;
    private Boolean valuationRequired;

    private BigDecimal maxLoanToValueRatio;
}
