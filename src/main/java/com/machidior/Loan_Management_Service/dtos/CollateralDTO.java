package com.machidior.Loan_Management_Service.dtos;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollateralDTO {
    private String type;
    private String description;
    private String document;
    private BigDecimal valuation;
}
