package com.machidior.Loan_Management_Service.dtos.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuarantorRequest {

    private String name;
    private String relationship;
    private String phoneNumber;
    private String nationalId;
    private String gender;
    private String email;
    private String occupation;
    private Integer age;
    private String address;
    private String guarantee;
    private BigDecimal guaranteeValue;
    private BigDecimal monthlyIncome;
}
