package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KuzaLoanCollateralResponse {

    private Long id;

    private String CustomerId;
    private CollateralType type;
    private String name;
    private String description;
    private String location;
    private CollateralPurchaseCondition purchaseCondition;
    private CollateralCondition condition;
    private String photo;
    private LocalDate purchaseDate;
    private Integer quantity;
    private BigDecimal purchasingValue;
    private BigDecimal estimatedValue;

    private String applicationNumber;
}
