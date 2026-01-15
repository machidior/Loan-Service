package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.calculator.ChargeCalculator;
import com.machidior.Loan_Management_Service.dtos.response.ChargePreviewResponse;
import com.machidior.Loan_Management_Service.feign.ProductCharge;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargePreviewService {

    private final ProductConfigurationsClient client;
    private final ChargeCalculator calculator;

    public List<ChargePreviewResponse> previewCharges(
            LoanApplication application
    ) {

        List<ProductCharge> charges =
                client.getVersionCharges(
                        application.getProductVersionId()
                );

        return charges.stream()
                .map(policy -> {

                    BigDecimal amount =
                            calculator.calculate(policy, application);

                    return new ChargePreviewResponse(
                            policy.getName(),
                            policy.getType(),
                            amount
                    );
                })
                .toList();
    }
}
