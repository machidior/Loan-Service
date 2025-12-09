package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.Gender;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KuzaLoanGuarantorResponse {

    private Long id;

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
    private BigDecimal guaranteeValue;

    private String passportUrl;
    private String identificationCardUrl;

    private boolean approved;

    private String applicationNumber;
}
