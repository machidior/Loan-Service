package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.DisbursementStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "disbursements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    private String customerId;
    @Enumerated(EnumType.STRING)
    private LoanProductType loanProductType;
    private String accountNumber;
    private BigDecimal amountDisbursed;
    private String disbursementMethod;
    private String transactionReference;
    @Enumerated(EnumType.STRING)
    private DisbursementStatus status;
    private LocalDate disbursementDate;
    private String disbursedBy;
    private String remarks;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    
}
