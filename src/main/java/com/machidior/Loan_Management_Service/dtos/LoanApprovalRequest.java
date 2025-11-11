package com.machidior.Loan_Management_Service.dtos;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApprovalRequest {
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private String comments;
}
