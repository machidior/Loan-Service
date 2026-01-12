package com.machidior.Loan_Management_Service.evaluator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuarantorItemData {
    private BigDecimal income;
    private Boolean employmentProvided;
    private Boolean relationProvided;
    private Boolean incomeProofProvided;

    private Boolean passportPhotoProvided;
    private Boolean idDocumentProvided;
    private Boolean guarantorConsentProvided;
}
