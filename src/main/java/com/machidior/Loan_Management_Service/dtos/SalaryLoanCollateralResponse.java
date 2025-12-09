package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryLoanCollateralResponse {
    private Long id;

    private String CustomerId;
    private CollateralType type;
    private String name;
    private String description;
    private String location;
    private CollateralPurchaseCondition purchaseCondition;
    private CollateralCondition condition;
    private String photoUrl;
    private LocalDate purchaseDate;
    private Integer quantity;
    private BigDecimal purchasingValue;
    private BigDecimal estimatedValue;

    private String applicationNumber;
}
