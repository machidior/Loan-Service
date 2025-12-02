package com.machidior.Loan_Management_Service.dtos;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationApprovalRequest {
    private BigDecimal approvedAmount;
    private String comments;
}
