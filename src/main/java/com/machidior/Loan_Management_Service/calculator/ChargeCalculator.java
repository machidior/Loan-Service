package com.machidior.Loan_Management_Service.calculator;

import com.machidior.Loan_Management_Service.feign.ProductCharge;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;



@Component
public class ChargeCalculator {

    public BigDecimal calculate(
            ProductCharge policy,
            LoanApplication application
    ) {
        return switch (policy.getCalculationMethod()) {

            case FIXED -> policy.getFixedAmount();

            case PERCENTAGE_OF_PRINCIPAL ->
                    application.getAmountRequested()
                            .multiply(policy.getPercentage())
                            .divide(BigDecimal.valueOf(100));

            default -> throw new IllegalStateException(
                    "Unsupported charge type: " + policy.getType()
            );
        };
    }
}
