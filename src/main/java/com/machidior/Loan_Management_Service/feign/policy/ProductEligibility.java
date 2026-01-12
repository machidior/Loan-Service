package com.machidior.Loan_Management_Service.feign.policy;

import com.machidior.Loan_Management_Service.enums.ClientType;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEligibility {

    private Long id;

    private Integer minApplicantAge;
    private Integer maxApplicantAge;

    private BigDecimal minMonthlyIncome;
    private Integer minBusinessAgeMonths;

    private List<ClientType> allowedClientTypes;

    private Integer minEmploymentDurationMonths;

    private List<String> allowedRegions;

    private Boolean allowExistingBorrowers;
    private Boolean allowFirstTimeBorrowers;
}
