package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loan_product_terms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanProductTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoanProductType productType;

    @Column(name = "min_amount")
    private BigDecimal minAmount;

    @Column(name = "max_amount")
    private BigDecimal maxAmount;

    @Column(name = "maximum_term_months")
    private Integer maximumTermMonths;

    @Column(name = "total_interest_rate")
    private BigDecimal totalInterestRatePerMonth;

    @Column(name = "total_interest_rate_2month")
    private BigDecimal totalInterestPer2Month;

    @Column(name = "interest_rate")
    private BigDecimal monthlyInterestRate;

    @Column(name = "min_group_members", nullable=true)
    private Integer minGroupMembers;

    @Column(name = "max_group_members", nullable = true)
    private Integer maxGroupMembers;

    @Column(name = "extra_rules", columnDefinition = "TEXT")
    private String extraRules;
}
