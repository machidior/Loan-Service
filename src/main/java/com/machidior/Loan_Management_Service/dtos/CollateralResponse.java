package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollateralResponse {

    private Long id;

    private String CustomerId;
    private CollateralType type;
    private String name;
    private String description;
    private String location;
    private CollateralPurchaseCondition purchaseCondition;
    private CollateralCondition condition;
    private String ownershipProofUrl;
    private String photoUrl;
    private LocalDate purchaseDate;
    private Integer quantity;
    private BigDecimal purchasingValue;
    private BigDecimal estimatedValue;

    private String applicationNumber;
}
