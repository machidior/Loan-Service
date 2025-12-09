package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessLoanApplicationResponse {

    private String applicationNumber;
    private String customerId;
    private String loanOfficerId;
    private LoanProductType productType;

    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    private BigDecimal loanFeeRate;
    private Integer termMonths;
    private InstallmentFrequency installmentFrequency;
    private LoanApplicationStatus status;

    private BigDecimal applicationFee;
    private BigDecimal loanInsuranceFee;

    private Boolean isRead;
    private String purpose;
    private String remarks;

    private String bankStatementUrl;
    private String insuranceComprehensiveCoverUrl;
    private String businessLicenseUrl;
    private String tinCertificateUrl;
    private String brelaCertificateUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<BusinessDetails> businessDetails;
    private List<BusinessLoanCollateralResponse> collaterals;
    private BusinessLoanGuarantorResponse guarantor;
}
