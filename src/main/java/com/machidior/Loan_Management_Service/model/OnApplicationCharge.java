package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.ChargeType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnApplicationCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chargeName;
    @Enumerated(EnumType.STRING)
    private ChargeType type;
    private BigDecimal amount;

    private Boolean applied;

    @ManyToOne(fetch = FetchType.LAZY)
    private LoanApplication loanApplication;
}
