package com.machidior.Loan_Management_Service.model.requirement;

import com.machidior.Loan_Management_Service.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assetType;
    private String description;
    private BigDecimal estimatedValue;

    private Boolean ownershipVerified;
    private Boolean insured;

    private String ownershipProofUrl;
    private String valuationReportUrl;
    private String insuranceDocumentUrl;

    private Boolean approved;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_number")
    private LoanApplication loanApplication;
}
