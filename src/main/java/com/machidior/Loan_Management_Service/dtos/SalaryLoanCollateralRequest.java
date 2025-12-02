package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoanCollateralRequest {

    private String type;
    private String name;
    private String description;
    private String location;
    private String purchaseCondition;
    private String condition;
    private String photo;
    private LocalDate purchaseDate;
    private Integer quantity;
    private BigDecimal purchasingValue;
    private BigDecimal estimatedValue;
}
