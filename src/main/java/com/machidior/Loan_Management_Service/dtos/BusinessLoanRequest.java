package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.RepaymentFrequency;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLoanRequest {

    private String customerId;
    private BigDecimal amountRequested;
    private BigDecimal interestRate;
    private Integer termMonths;
    private RepaymentFrequency repaymentFrequency;
    private String purpose;
    private String loanOfficerId;
    private String remarks;

    private List<BusinessDetails> businessDetails;
    private List<BusinessLoanCollateralRequest> collaterals;
    private BusinessLoanGuarantorRequest guarantor;
}
