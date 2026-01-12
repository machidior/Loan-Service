package com.machidior.Loan_Management_Service.dtos.response;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.model.AgricultureRequirementDetails;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import com.machidior.Loan_Management_Service.model.EmploymentDetails;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApplicationResponse {

    private String applicationNumber;

    private String customerId;
    private String loanOfficerId;
    private Long productId;
    private String productName;

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

    private List<GuarantorResponse> guarantors;

    private List<CollateralResponse> collaterals;

    private EmploymentDetails employmentDetails;

    private BusinessDetails businessDetails;

    private AgricultureRequirementDetails agricultureRequirementDetails;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
