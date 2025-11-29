package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusinessLoanMapper {

    private final BusinessLoanCollateralMapper collateralMapper;
    private final BusinessLoanGuarantorMapper guarantorMapper;

    public BusinessLoan toEntity(BusinessLoanRequest request) {
        BusinessLoan loan = BusinessLoan.builder()
                .customerId(request.getCustomerId())
                .amountRequested(request.getAmountRequested())
                .interestRate(request.getInterestRate())
                .paymentDuration(request.getPaymentDuration())
                .repaymentFrequency(request.getRepaymentFrequency())
                .purpose(request.getPurpose())
                .loanOfficerId(request.getLoanOfficerId())
                .remarks(request.getRemarks())
                .status(LoanStatus.PENDING)
                .build();

        if (request.getBusinessDetails() != null) {
            request.getBusinessDetails().forEach(detail -> detail.setBusinessLoan(loan));
            loan.setBusinessDetails(request.getBusinessDetails());
        }


        if (request.getCollaterals() != null) {
            List<BusinessLoanCollateral> collaterals = request.getCollaterals()
                    .stream()
                    .map(req -> collateralMapper.toEntity(req, loan))
                    .collect(Collectors.toList());
            loan.setCollaterals(collaterals);
        }

        if (request.getGuarantor() != null) {
            BusinessLoanGuarantor guarantor = guarantorMapper.toEntity(request.getGuarantor(), loan);
            loan.setGuarantor(guarantor);
        }

        return loan;
    }

    public BusinessLoanResponse toResponse(BusinessLoan loan) {
        return BusinessLoanResponse.builder()
                .id(loan.getId())
                .applicationNumber(loan.getApplicationNumber())
                .customerId(loan.getCustomerId())
                .amountRequested(loan.getAmountRequested())
                .amountApproved(loan.getAmountApproved())
                .interestRate(loan.getInterestRate())
                .paymentDuration(loan.getPaymentDuration())
                .repaymentFrequency(loan.getRepaymentFrequency())
                .purpose(loan.getPurpose())
                .status(loan.getStatus())
                .loanOfficerId(loan.getLoanOfficerId())
                .remarks(loan.getRemarks())
                .applicationFee(loan.getApplicationFee())
                .loanInsuranceFee(loan.getLoanInsuranceFee())
                .totalPayableAmount(loan.getTotalPayableAmount())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .businessDetails(loan.getBusinessDetails())
                .collaterals(loan.getCollaterals() != null
                        ? loan.getCollaterals().stream()
                        .map(collateralMapper::toResponse)
                        .collect(Collectors.toList())
                        : null)
                .guarantor(loan.getGuarantor() != null
                        ? guarantorMapper.toResponse(loan.getGuarantor())
                        : null)
                .build();
    }
}
