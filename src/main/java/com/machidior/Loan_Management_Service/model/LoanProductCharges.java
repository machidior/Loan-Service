package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loan_product_charges")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanProductCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoanProductType productType;

    @Column(name = "first_application_fee")
    private BigDecimal firstApplicationFee;

    @Column(name = "subsequent_application_fee")
    private BigDecimal subsequentApplicationFee;

    @Column(name = "loan_insurance_percent")
    private BigDecimal loanInsurancePercent;

    @Column(name = "group_insurance_percent")
    private BigDecimal groupInsurancePercent;

    @Column(name = "extra_rules", columnDefinition = "TEXT")
    private String extraRules;
}
