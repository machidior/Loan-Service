package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "business_loan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLoan {
    @Id
    @GeneratedValue(generator = "business_loan_id_generator")
    @GenericGenerator(
            name = "business_loan_id_generator",
            strategy = "com.machidior.Loan_Management_Service.generator.BusinessLoanIdGenerator"
    )
    @Column(length = 20)
    private String id;
    private String applicationNumber;
    @Enumerated(EnumType.STRING)
    private LoanProductType productType;
    private String customerId;
    private BigDecimal principal;
    private BigDecimal interestRate;
    private BigDecimal loanFeeRate;
    @Enumerated(EnumType.STRING)
    private InstallmentFrequency installmentFrequency;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    private BigDecimal totalPayableAmount;
    private Integer termMonths;
    private String loanContract;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
