package com.machidior.Loan_Management_Service.dtos.response;

import com.machidior.Loan_Management_Service.enums.ChargeType;

import java.math.BigDecimal;

public record ChargePreviewResponse(
        String name,
        ChargeType type,
        BigDecimal amount
) {}
