package com.machidior.Loan_Management_Service.completion;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class RequirementMetadataExtractor {

    public Map<RequirementType, RequirementMetadata> extract(
            Requirements requirements
    ) {

        Map<RequirementType, RequirementMetadata> map =
                new EnumMap<>(RequirementType.class);

        put(map, RequirementType.AGRICULTURE, requirements.getAgricultureRequirement());
        put(map, RequirementType.SOLIDARITY, requirements.getSolidarityRequirement());
        put(map, RequirementType.ASSET, requirements.getAssetRequirement());
        put(map, RequirementType.BUSINESS_DETAILS, requirements.getBusinessRequirement());
        put(map, RequirementType.COLLATERAL, requirements.getCollateralRequirement());
        put(map, RequirementType.DIGITAL_CONSENT, requirements.getDigitalConsentRequirement());
        put(map, RequirementType.EMPLOYMENT_DETAILS, requirements.getEmploymentRequirement());
        put(map, RequirementType.GUARANTOR, requirements.getGuarantorRequirement());
        put(map, RequirementType.EDUCATION, requirements.getEducationRequirement());
        put(map, RequirementType.FINANCIAL_HISTORY, requirements.getFinancialHistoryRequirement());
        put(map, RequirementType.HOUSING, requirements.getHousingRequirement());

        return map;
    }

    private void put(
            Map<RequirementType, RequirementMetadata> map,
            RequirementType type,
            ProductRequirementConfig requirement
    ) {

        if (requirement == null) return;

        boolean enabled = Boolean.TRUE.equals(requirement.getEnabled());
        boolean mandatory = Boolean.TRUE.equals(requirement.getMandatory());

        map.put(type, new RequirementMetadata(enabled, mandatory));
    }
}

