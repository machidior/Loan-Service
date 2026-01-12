package com.machidior.Loan_Management_Service.feign.policy;


import com.machidior.Loan_Management_Service.enums.PenaltyCalculationMethod;
import com.machidior.Loan_Management_Service.enums.PenaltyFrequency;
import com.machidior.Loan_Management_Service.enums.PenaltyType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PenaltyPolicy {

    private Long id;

    private Integer graceDaysAfterDueDate;

    private PenaltyType penaltyType;

    private PenaltyCalculationMethod calculationMethod;

    private BigDecimal fixedAmount;

    private BigDecimal percentage;

    private BigDecimal maxPenaltyAmount;

    private PenaltyFrequency frequency;

    private Boolean compoundPenalty;

    private Integer maxPenaltyDays;

}
