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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_loan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoan {
    @Id
    @GeneratedValue(generator = "salary_loan_id_generator")
    @GenericGenerator(
            name = "salary_loan_id_generator",
            strategy = "com.machidior.Loan_Management_Service.generator.SalaryLoanIdGenerator"
    )
    private String id;

    private String applicationNumber;
    private String customerId;
    private BigDecimal principal;
    private BigDecimal interestRate;
    @Enumerated(EnumType.STRING)
    private LoanProductType productType;
    private BigDecimal loanFeeRate;
    @Enumerated(EnumType.STRING)
    private InstallmentFrequency installmentFrequency;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    private BigDecimal totalPayableAmount;
    private Integer termMonths;
    private String loanContractUrl;
    private LocalDate disbursedOn;
    private LocalDateTime approvedOn;
    private LocalDateTime appliedOn;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
