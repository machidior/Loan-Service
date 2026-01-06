package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApplicationRejectionResponse {

    private Long id;
    private String applicationNumber;
    private String rejectedBy;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
}
