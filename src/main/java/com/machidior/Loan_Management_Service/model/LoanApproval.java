package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_approvals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String approvedBy;
    private LocalDateTime approvedAt;

    private BigDecimal approvedAmount;
    private BigDecimal interestRate;

    private String comments;

//    @OneToOne
//    @JoinColumn(name = "loan_application_id")
//    private LoanApplication loanApplication;
}
