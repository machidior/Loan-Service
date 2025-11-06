package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loan_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private LoanProductType productType;

    private String description;

    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    private Integer minTermMonths;
    private Integer maxTermMonths;

    private BigDecimal baseInterestRate;

    private boolean requiresCollateral;
    private boolean requiresGuarantor;
    private boolean requiresGroup;
}
