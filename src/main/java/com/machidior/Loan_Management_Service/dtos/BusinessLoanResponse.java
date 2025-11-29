package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.enums.RepaymentFrequency;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLoanResponse {

    private String id;
    private String applicationNumber;
    private String customerId;

    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    private Integer paymentDuration;
    private RepaymentFrequency repaymentFrequency;
    private String purpose;

    private LoanStatus status;
    private String loanOfficerId;
    private String remarks;

    private BigDecimal applicationFee;
    private BigDecimal loanInsuranceFee;
    private BigDecimal totalPayableAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<BusinessDetails> businessDetails;
    private List<BusinessLoanCollateralResponse> collaterals;
    private BusinessLoanGuarantorResponse guarantor;
}
