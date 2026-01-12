package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collateral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CustomerId;
    @Enumerated(EnumType.STRING)
    private CollateralType type;
    private String name;
    private String description;
    private String location;
    @Enumerated(EnumType.STRING)
    private CollateralPurchaseCondition purchaseCondition;

    private String ownershipProofUrl;
    private String insuranceDocumentUrl;
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private CollateralCondition condition;
    private LocalDate purchaseDate;
    private Integer quantity;
    @Column(name = "purchasing_value")
    private BigDecimal purchasingValue;
    @Column(name = "estimated_value")
    private BigDecimal estimatedValue;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_application_number")
    private LoanApplication loanApplication;
}
