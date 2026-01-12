package com.machidior.Loan_Management_Service.feign.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgricultureRequirement {

    private Long id;
    private RequirementType type;
    private Boolean enabled;
    private Boolean mandatory;

    private Boolean farmDetailsRequired;
    private Boolean productionCycleRequired;
    private Boolean offTakerAgreement;
    private Boolean farmInspectionRequired;

    private Integer minFarmSize;
}
