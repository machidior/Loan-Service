package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessLoanApplicationRequest {


    private List<BusinessDetails> businessDetails;
    private List<BusinessLoanCollateralRequest> collaterals;
    private BusinessLoanGuarantorRequest guarantor;
}
