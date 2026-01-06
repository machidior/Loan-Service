package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDetailsRequest {

    private String tinNumber;
    List<BusinessRequest> businessList;
}
