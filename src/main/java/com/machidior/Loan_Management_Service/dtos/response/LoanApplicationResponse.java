package com.machidior.Loan_Management_Service.dtos.response;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.TenureUnit;
import com.machidior.Loan_Management_Service.model.OnApplicationCharge;
import com.machidior.Loan_Management_Service.model.requirement.AgricultureRequirementDetails;
import com.machidior.Loan_Management_Service.model.requirement.BusinessDetails;
import com.machidior.Loan_Management_Service.model.requirement.EmploymentDetails;
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
    private Long productVersionId;
    private String productName;
    private String productCode;

    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    private Integer termTenure;
    private TenureUnit tenureUnit;
    private InstallmentFrequency installmentFrequency;
    private LoanApplicationStatus status;

    private List<OnApplicationCharge> charges;
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
