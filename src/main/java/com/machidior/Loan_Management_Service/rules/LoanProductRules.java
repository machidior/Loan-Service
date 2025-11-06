package com.machidior.Loan_Management_Service.rules;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class LoanProductRules {

    private final Map<LoanProductType, ProductRule> rules = new EnumMap<>(LoanProductType.class);

    public LoanProductRules() {
        rules.put(LoanProductType.BUSINESS_PRODUCT, new ProductRule(true, true, false, 100000.0, 5000000.0, 3, 36));
        rules.put(LoanProductType.SALARY_PRODUCT, new ProductRule(false, true, false, 50000.0, 2000000.0, 3, 24));
        rules.put(LoanProductType.KUZA_CAPITAL, new ProductRule(true, true, false, 200000.0, 10000000.0, 6, 48));
        rules.put(LoanProductType.GROUP_PRODUCT, new ProductRule(false, true, true, 100000.0, 3000000.0, 3, 24));
        rules.put(LoanProductType.STAFF_PRODUCT, new ProductRule(false, false, false, 50000.0, 1000000.0, 3, 18));
        rules.put(LoanProductType.EXECUTIVE_PRODUCT, new ProductRule(true, true, false, 500000.0, 20000000.0, 12, 60));
    }

    public ProductRule getRule(LoanProductType type) {
        return rules.get(type);
    }

    public record ProductRule(
            boolean requiresCollateral,
            boolean requiresGuarantor,
            boolean requiresGroup,
            double minAmount,
            double maxAmount,
            int minTermMonths,
            int maxTermMonths
    ) {}
}
