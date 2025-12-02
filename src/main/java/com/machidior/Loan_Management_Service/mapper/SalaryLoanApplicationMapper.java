package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.SalaryLoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanApplicationResponse;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SalaryLoanApplicationMapper {

    private final SalaryLoanCollateralMapper collateralMapper;
    private final SalaryLoanGuarantorMapper guarantorMapper;

    public SalaryLoanApplication toEntity(SalaryLoanApplicationRequest request) {
        SalaryLoanApplication application = SalaryLoanApplication.builder()
                .customerId(request.getCustomerId())
                .amountRequested(request.getAmountRequested())
                .termMonths(request.getTermMonths())
                .installmentFrequency(request.getInstallmentFrequency())
                .purpose(request.getPurpose())
                .loanOfficerId(request.getLoanOfficerId())
                .remarks(request.getRemarks())
                .status(LoanApplicationStatus.PENDING)
                .jobDetails(request.getJobDetails())
                .build();


        if (request.getCollateralRequests() != null) {
            List<SalaryLoanCollateral> collaterals = request.getCollateralRequests()
                    .stream()
                    .map(req -> collateralMapper.toEntity(req, application))
                    .collect(Collectors.toList());
            application.setCollaterals(collaterals);
        }

        if (request.getGuarantorRequest() != null) {
            SalaryLoanGuarantor guarantor = guarantorMapper.toEntity(request.getGuarantorRequest(), application);
            application.setGuarantor(guarantor);
        }

        return application;
    }

    public SalaryLoanApplicationResponse toResponse(SalaryLoanApplication application) {
        return SalaryLoanApplicationResponse.builder()
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
                .jobDetails(application.getJobDetails())
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
