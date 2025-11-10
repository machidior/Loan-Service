package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.enums.RepaymentFrequency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoanResponse {
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String bankStatementUrl;
    private String salarySlip;
    private String employmentLetter;
    private String insuranceComprehensiveCover;
    private String jobContract;
}
