package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.SalaryLoanRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanResponse;
import com.machidior.Loan_Management_Service.model.SalaryLoan;

public class SalaryLoanMapper {

    public static SalaryLoan toEntity(SalaryLoanRequest request) {
        return SalaryLoan.builder()
                .customerId(request.getCustomerId())
                .amountRequested(request.getAmountRequested())
                .amountApproved(request.getAmountApproved())
                .interestRate(request.getInterestRate())
                .termMonths(request.getTermMonths())
                .repaymentFrequency(request.getRepaymentFrequency())
                .purpose(request.getPurpose())
                .loanOfficerId(request.getLoanOfficerId())
                .remarks(request.getRemarks())
                .bankStatementUrl(request.getBankStatementUrl())
                .salarySlip(request.getSalarySlip())
                .employmentLetter(request.getEmploymentLetter())
                .insuranceComprehensiveCover(request.getInsuranceComprehensiveCover())
                .jobContract(request.getJobContract())
                .build();
    }

    public static SalaryLoanResponse toResponse(SalaryLoan loan) {
        return SalaryLoanResponse.builder()
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
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .bankStatementUrl(loan.getBankStatementUrl())
                .salarySlip(loan.getSalarySlip())
                .employmentLetter(loan.getEmploymentLetter())
                .insuranceComprehensiveCover(loan.getInsuranceComprehensiveCover())
                .jobContract(loan.getJobContract())
                .build();
    }
}
