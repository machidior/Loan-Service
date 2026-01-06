package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessRequest {

    private String businessName;
    private String businessType;
    private String businessAddress;
    private String businessSector;
    private Integer yearsInOperation;
}
