package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.DisbursementStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    private Long productId;
    private String productName;
    private String accountNumber;
    private BigDecimal amountDisbursed;
    private String disbursementMethod;
    private String transactionReference;
    @Enumerated(EnumType.STRING)
    private DisbursementStatus status;
    private LocalDate disbursementDate;
    private String disbursedBy;
    private String remarks;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    
}
