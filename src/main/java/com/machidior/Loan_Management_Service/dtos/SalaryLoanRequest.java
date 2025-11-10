package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.RepaymentFrequency;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoanRequest {
    private String customerId;
    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    private Integer termMonths;
    private RepaymentFrequency repaymentFrequency;
    private String purpose;
    private String loanOfficerId;
    private String remarks;

    private String bankStatementUrl;
    private String salarySlip;
    private String employmentLetter;
    private String insuranceComprehensiveCover;
    private String jobContract;
}
