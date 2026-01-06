package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDetails {

    private Long productId;
    private String productName;
    private String customerId;
    private BigDecimal amountRequested;
    private Integer termMonths;
    private InstallmentFrequency installmentFrequency;
    private String purpose;
    private String loanOfficerId;
    private String remarks;
}
