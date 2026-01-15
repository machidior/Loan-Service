package com.machidior.Loan_Management_Service.model.requirement;

import com.machidior.Loan_Management_Service.enums.HousingType;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "housing_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Housing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HousingType housingType;
    private String address;
    private Integer residenceDurationMonths;

    private Boolean rentAgreementProvided;
    private String rentAgreementUrl;

    private Boolean ownershipProofProvided;
    private String ownershipProofUrl;

    private Boolean buildingPlanProvided;
    private String buildingPlanUrl;

    private Boolean siteInspectionReportProvided;
    private String siteInspectionReportUrl;

    private BigDecimal propertyValuationAmount;

    private Boolean utilityBillProvided;
    private String utilityBillUrl;

    private Boolean approved;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    private LoanApplication loanApplication;

}

