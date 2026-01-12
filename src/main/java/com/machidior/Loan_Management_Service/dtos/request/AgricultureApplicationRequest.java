package com.machidior.Loan_Management_Service.dtos.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgricultureApplicationRequest {

    private String farmLocation;
    private String cropType;
    private BigDecimal farmSize;
    private String farmSizeUnit;
    private String landOwnershipType;

    private String productionCycle;
    private Integer productionDurationMonths;
    private BigDecimal expectedYield;
    private String yieldUnit;

    private Boolean hasOffTaker;
    private String offTakerName;
    private String offTakerContractRef;

    private Boolean farmInspected;
    private LocalDate inspectionDate;
    private String inspectorName;
    private String inspectionRemarks;

    private Boolean farmDetailsProvided;
    private Boolean productionCycleProvided;
    private Boolean offTakerAgreementProvided;
}
