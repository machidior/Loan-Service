package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.Gender;
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
    private Gender gender;
    private String email;
    private String occupation;
    private Integer age;
    private String address;
    private String guarantee;
    private BigDecimal GuaranteeValue;
}
