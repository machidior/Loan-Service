package com.machidior.Loan_Management_Service.feign.requirement;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Requirements {
    private AgricultureRequirement agricultureRequirement;
    private SolidarityRequirement solidarityRequirement;
    private AssetRequirement assetRequirement;
    private BusinessRequirement businessRequirement;
    private CollateralRequirement collateralRequirement;
    private DigitalConsentRequirement digitalConsentRequirement;
    private EmploymentRequirement employmentRequirement;
    private GuarantorRequirement guarantorRequirement;
    private EducationRequirement educationRequirement;
    private FinancialHistoryRequirement financialHistoryRequirement;
    private HousingRequirement housingRequirement;



//    private IdentityRequirementResponse identityRequirement;
//    private LegalRequirementResponse legalRequirement;
//    private ReferencesRequirementResponse referencesRequirement;
}
