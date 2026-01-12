package com.machidior.Loan_Management_Service.evaluator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollateralRequirementData {
    private BigDecimal requestedAmount;
    private BigDecimal totalCollateralValue;
    private List<CollateralItemData> collateralItems;
}
