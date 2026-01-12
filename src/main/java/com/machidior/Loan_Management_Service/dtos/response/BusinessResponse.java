package com.machidior.Loan_Management_Service.dtos.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessResponse {

    private String businessName;
    private String businessType;
    private String businessAddress;
    private String businessSector;
    private Integer yearsInOperation;
    private Long businessDetailsId;
}
