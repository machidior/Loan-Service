package com.machidior.Loan_Management_Service.dtos.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmploymentRequest {

    private String employerName;
    private String employmentType;
    private String employerAddress;
    private String employerContactNumber;
    private String jobTitle;

    private BigDecimal monthlyIncome;
    private BigDecimal netMonthlyIncome;

    private LocalDate employmentStartDate;
    private Integer employmentDurationMonths;
}
