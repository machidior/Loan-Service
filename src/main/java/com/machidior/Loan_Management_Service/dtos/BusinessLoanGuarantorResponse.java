package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.Gender;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessLoanGuarantorResponse {
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
    private BigDecimal GuaranteeValue;

    private String passportUrl;
    private String identificationCardUrl;

    private boolean approved;

    private String applicationNumber;
}
