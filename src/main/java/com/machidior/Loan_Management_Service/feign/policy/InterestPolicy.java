package com.machidior.Loan_Management_Service.feign.policy;


import com.machidior.Loan_Management_Service.enums.DayCountConversion;
import com.machidior.Loan_Management_Service.enums.InterestAccrualMethod;
import com.machidior.Loan_Management_Service.enums.InterestType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterestPolicy {

    private Long id;

    private InterestType interestType;

    private BigDecimal annualInterestRate;

    private BigDecimal minInterestRate;
    private BigDecimal maxInterestRate;

    private Boolean allowedRateOverride;

    private InterestAccrualMethod accrualMethod;

    private DayCountConversion dayCountConversion;

    private Boolean accrueDuringGracePeriod;

    private Boolean capitalizedInterest;

}
