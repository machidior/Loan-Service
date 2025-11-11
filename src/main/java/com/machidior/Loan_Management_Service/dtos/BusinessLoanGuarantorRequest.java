package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLoanGuarantorRequest {
    private String name;
    private String relationship;
    private String phoneNumber;
    private String nationalId;
    private String gender;
    private String email;
    private String occupation;
    private String age;
    private String address;
    private String guarantee;
    private BigDecimal GuaranteeValue;
}
