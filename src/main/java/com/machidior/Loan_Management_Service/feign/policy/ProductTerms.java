package com.machidior.Loan_Management_Service.feign.policy;


import com.machidior.Loan_Management_Service.enums.DisbursementMethod;
import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.TenureUnit;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTerms {

    private Long id;

    private BigDecimal minLoanAmount;

    private BigDecimal maxLoanAmount;

    private BigDecimal firstTimeBorrowerMaxAmount;

    private Integer minTenure;

    private Integer maxTenure;

    private TenureUnit tenureUnit;

    private List<InstallmentFrequency> allowedInstallmentFrequencies;

    private Integer minInstallments;
    private Integer maxInstallments;

    private Integer repaymentDayOfTheMonth;

    private Integer graceDaysBeforeFirstRepayment;

    private Boolean allowedCustomTenure;
    private Boolean allowedCustomAmount;

    private Boolean allowTopUp;

    private Boolean allowedRestructuring;

    private Boolean allowEarlyRepayment;
    private Boolean chargeEarlyRepaymentFee;

    private Boolean allowPartialDisbursement;
    private Integer maxDisbursementDaysAfterApproval;
    private Integer maxDisbursementTranches;
    private DisbursementMethod disbursementMethod;

    private Boolean requireCreditBureauCheck;
    private Integer maxAllowedActiveLoans;

    private Boolean requireManualApproval;
    private Boolean allowAutoApproval;
    private Integer approvalLevelCount;

    private Boolean allowBranchesOverride;

    private String currency;

}
