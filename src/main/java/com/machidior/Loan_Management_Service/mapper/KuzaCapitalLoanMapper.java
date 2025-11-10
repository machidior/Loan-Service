package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanResponse;
import com.machidior.Loan_Management_Service.dtos.KuzaCapitalLoanRequest;
import com.machidior.Loan_Management_Service.dtos.KuzaCapitalLoanResponse;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.KuzaCapitalLoan;
import org.springframework.stereotype.Component;

@Component
public class KuzaCapitalLoanMapper {

    public KuzaCapitalLoan toEntity(KuzaCapitalLoanRequest request) {
        if (request == null) return null;

        return KuzaCapitalLoan.builder()
                .customerId(request.getCustomerId())
                .amountRequested(request.getAmountRequested())
                .amountApproved(request.getAmountApproved())
                .interestRate(request.getInterestRate())
                .termMonths(request.getTermMonths())
                .repaymentFrequency(request.getRepaymentFrequency())
                .purpose(request.getPurpose())
                .loanOfficerId(request.getLoanOfficerId())
                .remarks(request.getRemarks())

                .bankStatement(request.getBankStatement())
                .insuranceComprehensiveCover(request.getInsuranceComprehensiveCover())
                .businessLicense(request.getBusinessLicense())
                .tinCertificate(request.getTinCertificate())
                .tinNumber(request.getTinNumber())
                .brelaCertificate(request.getBrelaCertificate())

                .status(LoanStatus.PENDING)
                .build();
    }

    public KuzaCapitalLoanResponse toResponse(KuzaCapitalLoan loan) {
        if (loan == null) return null;

        return KuzaCapitalLoanResponse.builder()
                .id(loan.getId())
                .applicationNumber(loan.getApplicationNumber())
                .customerId(loan.getCustomerId())
                .amountRequested(loan.getAmountRequested())
                .amountApproved(loan.getAmountApproved())
                .interestRate(loan.getInterestRate())
                .termMonths(loan.getTermMonths())
                .repaymentFrequency(loan.getRepaymentFrequency())
                .purpose(loan.getPurpose())
                .status(loan.getStatus())
                .loanOfficerId(loan.getLoanOfficerId())
                .remarks(loan.getRemarks())

                .bankStatement(loan.getBankStatement())
                .insuranceComprehensiveCover(loan.getInsuranceComprehensiveCover())
                .businessLicense(loan.getBusinessLicense())
                .tinCertificate(loan.getTinCertificate())
                .tinNumber(loan.getTinNumber())
                .brelaCertificate(loan.getBrelaCertificate())

                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }
}
