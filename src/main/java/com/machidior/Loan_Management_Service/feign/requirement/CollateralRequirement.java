package com.machidior.Loan_Management_Service.feign.requirement;


import com.machidior.Loan_Management_Service.completion.ProductRequirementConfig;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollateralRequirement implements ProductRequirementConfig {

    private Long id;
    private RequirementType type;
    private Boolean enabled;
    private Boolean mandatory;

    private List<CollateralType> allowedTypes;

    private Integer minCount;
    private Integer maxCount;

    private Boolean descriptionRequired;
    private Boolean ownershipProofRequired;
    private Boolean insuranceRequired;
    private Boolean valuationRequired;
    private Boolean photoRequired;

    private BigDecimal minLoanAmountToValueRatio;

}
