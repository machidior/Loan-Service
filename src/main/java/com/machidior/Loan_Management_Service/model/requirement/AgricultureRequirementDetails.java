package com.machidior.Loan_Management_Service.model.requirement;

import com.machidior.Loan_Management_Service.enums.RequirementStatus;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_app_agriculture_req")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgricultureRequirementDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequirementType type;

    // ---------- FARM DETAILS ----------
    private String farmLocation;
    private String cropType;
    private BigDecimal farmSize;
    private String farmSizeUnit; // ACRES, HECTARES
    private String landOwnershipType; // OWNED, LEASED, FAMILY

    // ---------- PRODUCTION ----------
    private String productionCycle; // SEASONAL, PERENNIAL
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

    @Enumerated(EnumType.STRING)
    private RequirementStatus status;

    private String rejectionReason;

    @OneToOne(cascade = CascadeType.ALL)
    private LoanApplication loanApplication;
}
