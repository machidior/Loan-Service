package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.LoanApplicationRejectionResponse;
import com.machidior.Loan_Management_Service.model.LoanApplicationRejection;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationRejectionMapper {

    public LoanApplicationRejectionResponse toResponse(LoanApplicationRejection rejection){

        return LoanApplicationRejectionResponse.builder()
                .id(rejection.getId())
                .rejectedBy(rejection.getRejectedBy())
                .applicationNumber(rejection.getApplicationNumber())
                .rejectedAt(rejection.getRejectedAt())
                .rejectionReason(rejection.getRejectionReason())
                .build();
    }
}
