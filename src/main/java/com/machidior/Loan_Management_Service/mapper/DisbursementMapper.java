package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.DisbursementResponse;
import com.machidior.Loan_Management_Service.model.Disbursement;
import org.springframework.stereotype.Component;

@Component
public class DisbursementMapper {

    public Disbursement toEntity(DisbursementRequest request){
        return  Disbursement.builder()
                .loanId(request.getLoanId())
                .transactionReference(request.getTransactionReference())
                .remarks(request.getRemarks())
                .disbursementDate(request.getDisbursementDate())
                .accountNumber(request.getAccountNumber())
                .disbursementMethod(request.getDisbursementMethod())
                .build();
    }

    public DisbursementResponse toResponse(Disbursement disbursement){
        return DisbursementResponse.builder()
                .id(disbursement.getId())
                .loanId(disbursement.getLoanId())
                .loanProductType(disbursement.getLoanProductType())
                .customerId(disbursement.getCustomerId())
                .accountNumber(disbursement.getAccountNumber())
                .transactionReference(disbursement.getTransactionReference())
                .status(disbursement.getStatus())
                .remarks(disbursement.getRemarks())
                .amountDisbursed(disbursement.getAmountDisbursed())
                .disbursementMethod(disbursement.getDisbursementMethod())
                .disbursementDate(disbursement.getDisbursementDate())
                .disbursedBy(disbursement.getDisbursedBy())
                .createdAt(disbursement.getCreatedAt())
                .modifiedAt(disbursement.getModifiedAt())
                .build();

    }
}
