package com.machidior.Loan_Management_Service.dtos.response;

import com.machidior.Loan_Management_Service.enums.DisbursementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisbursementResponse {
    private Long id;
    private String loanId;
    private String customerId;
    private Long productId;
    private String productName;
    private String accountNumber;
    private BigDecimal amountDisbursed;
    private String disbursementMethod;
    private String transactionReference;
    private DisbursementStatus status;
    private LocalDate disbursementDate;
    private String disbursedBy;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}
