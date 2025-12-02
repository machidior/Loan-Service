package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.Gender;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoanGuarantorResponse {
    private Long id;

    private String name;
    private String relationship;
    private String phoneNumber;
    private String nationalId;
    private Gender gender;
    private String email;
    private String occupation;
    private String age;
    private String address;
    private String guarantee;
    private BigDecimal GuaranteeValue;

    private boolean approved;

    private String applicationNumber;
}
