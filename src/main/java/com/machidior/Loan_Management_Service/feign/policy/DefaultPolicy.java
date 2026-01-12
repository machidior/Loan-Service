package com.machidior.Loan_Management_Service.feign.policy;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultPolicy {

    private Long id;

    private Integer daysOverdueToDefault;

    private Boolean freezeInterestAccrual;

    private Boolean freezePenaltyAccrual;

    private Boolean blockFurtherDisbursement;

    private Boolean markAsNonPerforming;

    private Integer maxAllowedOverdueDays;

    private Boolean blockIfAnyDefaultedLoan;
}
