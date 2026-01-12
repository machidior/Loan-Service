package com.machidior.Loan_Management_Service.evaluator.data;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgricultureRequirementData {
    private BigDecimal farmSize;
    private Boolean farmDetailsProvided;
    private Boolean productionCycleProvided;
    private Boolean offTakerAgreementProvided;
    private Boolean farmInspectionCompleted;
}
