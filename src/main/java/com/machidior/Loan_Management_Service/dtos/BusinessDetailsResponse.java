package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

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
    private String bankStatementUrl;
    private String insuranceComprehensiveCoverUrl;
    private String businessLicenseUrl;
    private String tinCertificateUrl;
    private String brelaCertificateUrl;
    private List<BusinessResponse> businesses;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
