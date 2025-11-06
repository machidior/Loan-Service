package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisbursementRequest {
    private String disbursedBy;
    private String accountNumber;
    private String disbursementMode; //  CASH, BANK_TRANSFER, MOBILE_MONEY
}
