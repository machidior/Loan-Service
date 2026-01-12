package com.machidior.Loan_Management_Service.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
