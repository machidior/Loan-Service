package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanApplicationResponse;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusinessLoanApplicationMapper {

    private final BusinessLoanCollateralMapper collateralMapper;
    private final BusinessLoanGuarantorMapper guarantorMapper;

    public BusinessLoanApplication toEntity(BusinessLoanApplicationRequest request) {
        BusinessLoanApplication application = BusinessLoanApplication.builder()
                .customerId(request.getCustomerId())
                .amountRequested(request.getAmountRequested())
                .termMonths(request.getTermMonths())
                .installmentFrequency(request.getInstallmentFrequency())
                .purpose(request.getPurpose())
                .loanOfficerId(request.getLoanOfficerId())
                .remarks(request.getRemarks())
                .status(LoanApplicationStatus.PENDING)
                .businessDetails(request.getBusinessDetails())
                .build();

        if (request.getBusinessDetails() != null) {
            request.getBusinessDetails().forEach(detail -> detail.setBusinessLoanApplication(application));
            application.setBusinessDetails(request.getBusinessDetails());
        }


        if (request.getCollaterals() != null) {
            List<BusinessLoanCollateral> collaterals = request.getCollaterals()
                    .stream()
                    .map(req -> collateralMapper.toEntity(req, application))
                    .collect(Collectors.toList());
            application.setCollaterals(collaterals);
        }

        if (request.getGuarantor() != null) {
            BusinessLoanGuarantor guarantor = guarantorMapper.toEntity(request.getGuarantor(), application);
            application.setGuarantor(guarantor);
        }

        return application;
    }

    public BusinessLoanApplicationResponse toResponse(BusinessLoanApplication application) {
        return BusinessLoanApplicationResponse.builder()
                .applicationNumber(application.getApplicationNumber())
                .customerId(application.getCustomerId())
                .amountRequested(application.getAmountRequested())
                .amountApproved(application.getAmountApproved())
                .interestRate(application.getInterestRate())
                .productType(application.getProductType())
                .isRead(application.getIsRead())
                .termMonths(application.getTermMonths())
                .installmentFrequency(application.getInstallmentFrequency())
                .purpose(application.getPurpose())
                .status(application.getStatus())
                .loanOfficerId(application.getLoanOfficerId())
                .remarks(application.getRemarks())
                .applicationFee(application.getApplicationFee())
                .loanInsuranceFee(application.getLoanInsuranceFee())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .businessDetails(application.getBusinessDetails())
                .collaterals(application.getCollaterals() != null
                        ? application.getCollaterals().stream()
                        .map(collateralMapper::toResponse)
                        .collect(Collectors.toList())
                        : null)
                .guarantor(application.getGuarantor() != null
                        ? guarantorMapper.toResponse(application.getGuarantor())
                        : null)
                .build();
    }
}
