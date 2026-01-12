package com.machidior.Loan_Management_Service.evaluator.data;

import com.machidior.Loan_Management_Service.enums.CollateralType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollateralItemData {

    private CollateralType type;

    private Boolean descriptionProvided;
    private Boolean ownershipProofProvided;
    private Boolean insuranceProvided;
    private Boolean valuationProvided;
    private Boolean photoProvided;
}

