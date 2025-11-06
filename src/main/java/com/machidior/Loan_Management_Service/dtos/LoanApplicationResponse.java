package com.machidior.Loan_Management_Service.dtos;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationResponse {
    private String applicationNumber;
    private String customerId;
    private String loanProductType;
    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private String status;
    private LocalDateTime createdAt;
    private List<CollateralDTO> collaterals;
    private List<GuarantorDTO> guarantors;
}
