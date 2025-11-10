package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.enums.RepaymentFrequency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLoanResponse {

    private String id;
    private String applicationNumber;
    private String customerId;
    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    private Integer termMonths;
    private RepaymentFrequency repaymentFrequency;
    private String purpose;
    private LoanStatus status;
    private String loanOfficerId;
    private String remarks;

    private String bankStatement;
    private String insuranceComprehensiveCover;
    private String businessLicense;
    private String tinCertificate;
    private String tinNumber;
    private String brelaCertificate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
