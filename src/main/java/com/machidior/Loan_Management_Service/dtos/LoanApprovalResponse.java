package com.machidior.Loan_Management_Service.dtos;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApprovalResponse {
    private Long id;
    private String loanId;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private String comments;
}
