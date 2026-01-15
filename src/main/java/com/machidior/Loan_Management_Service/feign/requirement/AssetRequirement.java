package com.machidior.Loan_Management_Service.feign.requirement;

import com.machidior.Loan_Management_Service.completion.ProductRequirementConfig;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetRequirement implements ProductRequirementConfig {

    private Long id;
    private RequirementType type;
    private Boolean enabled;
    private Boolean mandatory;

    private Boolean quotationRequired;
    private Boolean supplierVerificationRequired;
    private Boolean assetInsuranceRequired;
    private Boolean assetOwnershipTransferredToLender;
}
