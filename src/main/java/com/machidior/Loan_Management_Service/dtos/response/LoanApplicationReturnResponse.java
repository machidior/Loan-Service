package com.machidior.Loan_Management_Service.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApplicationReturnResponse {
    private Long id;
    private String applicationNumber;
    private String returnedBy;
    private LocalDateTime returnedAt;
    private String reasonOfReturn;
}
