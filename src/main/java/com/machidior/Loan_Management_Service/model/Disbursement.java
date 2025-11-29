package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Enumerated(EnumType.STRING)
    private LoanProductType loanProductType;
    private String accountNumber;
    private String disbursementMethod;
    private LocalDate disbursementDate;
    private String disbursedBy;
    @CreationTimestamp
    private LocalDateTime disbursedAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    /**
     * What this class should have
     *  private String disbursementId;
     *     private String loanId;
     *     private String customerId;
     *
     *     private LocalDateTime disbursementDate;
     *     private BigDecimal amountDisbursed;
     *     private BigDecimal principalAmount;
     *     private BigDecimal netAmountTransferred;
     *
     *     private BigDecimal processingFee;
     *     private BigDecimal insuranceFee;
     *     private BigDecimal exciseDuty;
     *     private BigDecimal otherCharges;
     *
     *     private BigDecimal feesDeductedAtDisbursement;
     *
     *     private String disbursementMethod; // CASH, BANK_TRANSFER, MOBILE_MONEY
     *     private String transactionReference;
     *
     *     private String createdBy;
     *     private String approvedBy;
     *     private LocalDateTime approvalDate;
     *
     *     private String status; // PENDING, DISBURSED, FAILED
     *     private String failureReason;
     *
     *     private LocalDateTime createdAt;
     *     private LocalDateTime updatedAt;
     *     private String remarks;
     *
     *     ⭐ Recommended Minimum Required Fields
     *
     * If you want a simple MVP, the minimum attributes are:
     *
     * loanId
     *
     * amountDisbursed
     *
     * disbursementDate
     *
     * disbursementMethod
     *
     * transactionReference
     *
     * status
     */
}
