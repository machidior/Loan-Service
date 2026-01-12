package com.machidior.Loan_Management_Service.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationApprovalRequest {
    private BigDecimal approvedAmount;
    private String comments;
}
