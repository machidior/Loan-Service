package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "collaterals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collateral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;          // e.g. "Car", "Land", "Title Deed"
    private String description;
    private String document;      // e.g. scanned document reference
    private BigDecimal valuation;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "loan_application_id")
//    private LoanApplication loanApplication;
}
