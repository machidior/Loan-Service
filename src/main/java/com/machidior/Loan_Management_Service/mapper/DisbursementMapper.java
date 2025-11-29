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
                .accountNumber(disbursement.getAccountNumber())
                .disbursementMethod(disbursement.getDisbursementMethod())
                .disbursementDate(disbursement.getDisbursementDate())
                .disbursedBy(disbursement.getDisbursedBy())
                .disbursedAt(disbursement.getDisbursedAt())
                .modifiedAt(disbursement.getModifiedAt())
                .build();

    }
}
