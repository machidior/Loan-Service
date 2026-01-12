package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.request.ApplicationDetails;
import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanApplicationMapper {

    private final CollateralMapper collateralMapper;
    private final GuarantorMapper guarantorMapper;

    public LoanApplication toEntity(ApplicationDetails request) {
        return LoanApplication.builder()
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .productName(request.getProductName())
                .amountRequested(request.getAmountRequested())
                .termMonths(request.getTermMonths())
                .installmentFrequency(request.getInstallmentFrequency())
                .purpose(request.getPurpose())
                .loanOfficerId(request.getLoanOfficerId())
                .remarks(request.getRemarks())
                .build();
    }

    public LoanApplicationResponse toResponse(LoanApplication application) {
        return LoanApplicationResponse.builder()
                .applicationNumber(application.getApplicationNumber())
                .customerId(application.getCustomerId())
                .amountRequested(application.getAmountRequested())
                .amountApproved(application.getAmountApproved())
                .interestRate(application.getInterestRate())
                .productId(application.getProductId())
                .productName(application.getProductName())
                .isRead(application.getIsRead())
                .termMonths(application.getTermMonths())
                .installmentFrequency(application.getInstallmentFrequency())
                .purpose(application.getPurpose())
                .status(application.getStatus())
                .loanOfficerId(application.getLoanOfficerId())
                .remarks(application.getRemarks())
                .loanFeeRate(application.getLoanFeeRate())
                .applicationFee(application.getApplicationFee())
                .loanInsuranceFee(application.getLoanInsuranceFee())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .collaterals(application.getCollaterals() != null
                        ? application.getCollaterals().stream()
                        .map(collateralMapper::toResponse)
                        .collect(Collectors.toList())
                        : null)
                .guarantors(application.getGuarantors() != null
                        ? application.getGuarantors().stream()
                        .map(guarantorMapper::toResponse)
                        .collect(Collectors.toList())
                        : null)
                .businessDetails(application.getBusinessDetails())
                .employmentDetails(application.getEmploymentDetails())
                .agricultureRequirementDetails(application.getAgricultureRequirementDetails())
                .build();
    }
}
