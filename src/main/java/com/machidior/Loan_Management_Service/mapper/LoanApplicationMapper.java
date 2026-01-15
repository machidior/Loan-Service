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
                .productVersionId(request.getProductVersionId())
                .productName(request.getProductName())
                .productCode(request.getProductCode())
                .amountRequested(request.getAmountRequested())
                .termTenure(request.getTermTenure())
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
                .productVersionId(application.getProductVersionId())
                .productName(application.getProductName())
                .productCode(application.getProductCode())
                .isRead(application.getIsRead())
                .termTenure(application.getTermTenure())
                .tenureUnit(application.getTenureUnit())
                .installmentFrequency(application.getInstallmentFrequency())
                .purpose(application.getPurpose())
                .status(application.getStatus())
                .loanOfficerId(application.getLoanOfficerId())
                .remarks(application.getRemarks())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }



    //                .collaterals(application.getCollaterals() != null
//                        ? application.getCollaterals().stream()
//                        .map(collateralMapper::toResponse)
//                        .collect(Collectors.toList())
//                        : null)
//                .guarantors(application.getGuarantors() != null
//                        ? application.getGuarantors().stream()
//                        .map(guarantorMapper::toResponse)
//                        .collect(Collectors.toList())
//                        : null)
//                .businessDetails(application.getBusinessDetails())
//                .employmentDetails(application.getEmploymentDetails())
//                .agricultureRequirementDetails(application.getAgricultureRequirementDetails())
}
