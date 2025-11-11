package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.LoanApprovalRequest;
import com.machidior.Loan_Management_Service.dtos.LoanApprovalResponse;
import com.machidior.Loan_Management_Service.model.LoanApproval;
import org.springframework.stereotype.Component;

@Component
public class LoanApprovalMapper {

    public LoanApproval toEntity(LoanApprovalRequest request, String loanId, String approvedBy) {
        return LoanApproval.builder()
                .loanId(loanId)
                .approvedBy(approvedBy)
                .approvedAmount(request.getApprovedAmount())
                .interestRate(request.getInterestRate())
                .comments(request.getComments())
                .build();
    }

    public LoanApprovalResponse toResponse(LoanApproval approval) {
        return LoanApprovalResponse.builder()
                .id(approval.getId())
                .loanId(approval.getLoanId())
                .approvedBy(approval.getApprovedBy())
                .approvedAt(approval.getApprovedAt())
                .approvedAmount(approval.getApprovedAmount())
                .interestRate(approval.getInterestRate())
                .comments(approval.getComments())
                .build();
    }
}
