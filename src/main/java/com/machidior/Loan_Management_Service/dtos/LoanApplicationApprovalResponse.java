package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApplicationApprovalResponse {
    private Long id;
    private String applicationNumber;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private BigDecimal approvedAmount;
    private String comments;
}
