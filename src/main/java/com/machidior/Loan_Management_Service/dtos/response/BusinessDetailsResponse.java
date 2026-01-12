package com.machidior.Loan_Management_Service.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDetailsResponse {

    private Long id;
    private String tinNumber;
    private BigDecimal averageMonthlyTurnOver;
    private String cashFlowStatementUrl;
    private String insuranceComprehensiveCoverUrl;
    private String businessLicenseUrl;
    private String tinCertificateUrl;
    private String brelaCertificateUrl;
    private List<BusinessResponse> businesses;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
