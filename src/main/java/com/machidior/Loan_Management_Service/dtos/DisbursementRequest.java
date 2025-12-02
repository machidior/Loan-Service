package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisbursementRequest {
    private String loanId;
    private String accountNumber;
    private String disbursementMethod;
    private String transactionReference;
    private LocalDate disbursementDate;
    private String remarks;
}
