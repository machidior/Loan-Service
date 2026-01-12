package com.machidior.Loan_Management_Service.dtos.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDetailsRequest {

    private String tinNumber;
    private BigDecimal averageMonthlyTurnover;
    List<BusinessRequest> businessList;
}
