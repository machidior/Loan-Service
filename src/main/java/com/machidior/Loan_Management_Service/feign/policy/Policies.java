package com.machidior.Loan_Management_Service.feign.policy;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Policies {
    private DefaultPolicy defaultPolicy;
    private ProductEligibility eligibilityPolicy;
    private InterestPolicy interestPolicy;
    private PenaltyPolicy penaltyPolicy;
    private ProductTerms termsPolicy;
}
