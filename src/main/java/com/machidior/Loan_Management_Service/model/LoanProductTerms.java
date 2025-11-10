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

    @Column(name = "payment_term")
    private String paymentTerm;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "repayment_duration_months")
    private Integer repaymentDurationMonths;

    @Column(name = "min_group_members", nullable=true)
    private Integer minGroupMembers;

    @Column(name = "max_group_members", nullable = true)
    private Integer maxGroupMembers;

    @Column(name = "extra_rules", columnDefinition = "TEXT")
    private String extraRules;
}
