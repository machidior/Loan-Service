package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.model.JobDetails;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoanApplicationRequest {

    private String customerId;
    private BigDecimal amountRequested;
    private Integer termMonths;
    private InstallmentFrequency installmentFrequency;
    private String purpose;
    private String loanOfficerId;
    private String remarks;

    private JobDetails jobDetails;
    private SalaryLoanGuarantorRequest guarantorRequest;
    private List<SalaryLoanCollateralRequest> collateralRequests;
}
