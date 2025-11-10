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
@Table(name = "executive_loan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutiveLoan {
    @Id
    @GeneratedValue(generator = "loan_id_generator")
    @GenericGenerator(
            name = "loan_id_generator",
            strategy = "com.machidior.Loan_Management_Service.generator.ExecutiveLoanIdGenerator"
    )
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

    // Relationships
//    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Guarantor> guarantors;
//
//    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Collateral> collaterals;
//
//    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RepaymentSchedule> repaymentSchedules;
}
