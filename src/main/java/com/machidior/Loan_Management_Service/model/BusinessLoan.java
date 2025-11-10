package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.enums.RepaymentFrequency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_loan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLoan {
    @Id
    @GeneratedValue(generator = "loan_id_generator")
    @GenericGenerator(
            name = "loan_id_generator",
            strategy = "com.machidior.Loan_Management_Service.generator.BusinessLoanIdGenerator"
    )
    @Column(length = 20)
    private String id;

    @Column(unique = true, nullable = false)
    private String applicationNumber;

    @Column(nullable = false)
    private String customerId; // Reference from customer-service

    @Column(nullable = false)
    private BigDecimal amountRequested;

    private BigDecimal amountApproved;
    private BigDecimal interestRate;

    private Integer termMonths;

    @Enumerated(EnumType.STRING)
    private RepaymentFrequency repaymentFrequency;

    private String purpose;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private String loanOfficerId;
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String bankStatement;
    private String insuranceComprehensiveCover;
    private String businessLicense;
    private String tinCertificate;
    private String tinNumber;
    private String brelaCertificate;

    private BigDecimal applicationFee;
    private BigDecimal loanInsuranceFee;
    private BigDecimal totalPayableAmount;

    /**
     *
     private String approvedBy;
     private LocalDateTime approvedAt;

     private String rejectedBy;
     private LocalDateTime rejectedAt;

     private String disbursedBy;
     private LocalDateTime disbursedAt;
        private String closedBy;
     private LocalDateTime closedAt;
     private String cancelledBy;
     private LocalDateTime cancelledAt;
     private LocalDateTime expectedDisbursementDate;

     Relationships
     @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<Guarantor> guarantors;

     @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<Collateral> collaterals;

     @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<RepaymentSchedule> repaymentSchedules;
     */

}
