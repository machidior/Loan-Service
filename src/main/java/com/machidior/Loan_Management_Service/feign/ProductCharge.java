package com.machidior.Loan_Management_Service.feign;


import com.machidior.Loan_Management_Service.enums.ChargeCalculationMethod;
import com.machidior.Loan_Management_Service.enums.ChargePayer;
import com.machidior.Loan_Management_Service.enums.ChargeTrigger;
import com.machidior.Loan_Management_Service.enums.ChargeType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCharge {

    private Long id;

    private String name;

    private ChargeType type;

    private ChargeCalculationMethod calculationMethod;

    private BigDecimal fixedAmount;

    private BigDecimal percentage;

    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    private ChargeTrigger trigger;

    private ChargePayer payer;

    private Boolean refundable;
    private Boolean mandatory;
    private Boolean allowOverride;
}
