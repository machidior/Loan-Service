package com.machidior.Loan_Management_Service.dtos;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationRequest {
    private String customerId;
    private String loanProductType;
    private BigDecimal amountRequested;
    private Integer termMonths;
    private String repaymentFrequency; // e.g. MONTHLY
    private String purpose;
    private String loanOfficerId;

    private List<CollateralDTO> collaterals;
    private List<GuarantorDTO> guarantors;
}
