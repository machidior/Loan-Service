package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationReturnResponse;
import com.machidior.Loan_Management_Service.model.LoanApplicationReturn;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationReturnMapper {

    public LoanApplicationReturnResponse toResponse(LoanApplicationReturn loanApplicationReturn){
        return LoanApplicationReturnResponse.builder()
                .id(loanApplicationReturn.getId())
                .applicationNumber(loanApplicationReturn.getApplicationNumber())
                .returnedBy(loanApplicationReturn.getReturnedBy())
                .returnedAt(loanApplicationReturn.getReturnedAt())
                .reasonOfReturn(loanApplicationReturn.getReasonOfReturn())
                .build();
    }
}
