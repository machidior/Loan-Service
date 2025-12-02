package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.model.KuzaBusinessDetails;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KuzaLoanApplicationRequest {

    private String customerId;
    private BigDecimal amountRequested;
    private Integer termMonths;
    private InstallmentFrequency installmentFrequency;
    private String purpose;
    private String loanOfficerId;
    private String remarks;

    private List<KuzaBusinessDetails> businessDetails;
    private List<KuzaLoanCollateralRequest> collaterals;
    private KuzaLoanGuarantorRequest guarantor;
}
