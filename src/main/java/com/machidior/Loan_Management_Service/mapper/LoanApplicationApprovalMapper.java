package com.machidior.Loan_Management_Service.mapper;


import com.machidior.Loan_Management_Service.dtos.LoanApplicationApprovalRequest;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationApprovalResponse;
import com.machidior.Loan_Management_Service.model.LoanApplicationApproval;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationApprovalMapper {

    public LoanApplicationApproval toEntity(LoanApplicationApprovalRequest request, String applicationNumber, String approvedBy) {
        return LoanApplicationApproval.builder()
                .applicationNumber(applicationNumber)
                .approvedBy(approvedBy)
                .approvedAmount(request.getApprovedAmount())
                .comments(request.getComments())
                .build();
    }

    public LoanApplicationApprovalResponse toResponse(LoanApplicationApproval approval) {
        return LoanApplicationApprovalResponse.builder()
                .id(approval.getId())
                .applicationNumber(approval.getApplicationNumber())
                .approvedBy(approval.getApprovedBy())
                .approvedAt(approval.getApprovedAt())
                .approvedAmount(approval.getApprovedAmount())
                .comments(approval.getComments())
                .build();
    }
}
