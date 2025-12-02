package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.SalaryLoanCollateralRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanCollateralResponse;
import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import com.machidior.Loan_Management_Service.model.*;
import org.springframework.stereotype.Component;

@Component
public class SalaryLoanCollateralMapper {

    public SalaryLoanCollateral toEntity(SalaryLoanCollateralRequest request, SalaryLoanApplication salaryLoanApplication) {
        return SalaryLoanCollateral.builder()
                .type(parseCollateralType(request.getType()))
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .purchaseCondition(parsePurchaseCondition(request.getPurchaseCondition()))
                .condition(parseCollateralCondition(request.getCondition()))
                .photo(request.getPhoto())
                .purchaseDate(request.getPurchaseDate())
                .quantity(request.getQuantity())
                .purchasingValue(request.getPurchasingValue())
                .estimatedValue(request.getEstimatedValue())
                .salaryLoanApplication(salaryLoanApplication)
                .build();
    }

    public SalaryLoanCollateralResponse toResponse(SalaryLoanCollateral collateral) {
        return SalaryLoanCollateralResponse.builder()
                .id(collateral.getId())
                .CustomerId(collateral.getCustomerId())
                .type(collateral.getType())
                .name(collateral.getName())
                .description(collateral.getDescription())
                .location(collateral.getLocation())
                .purchaseCondition(collateral.getPurchaseCondition())
                .condition(collateral.getCondition())
                .photo(collateral.getPhoto())
                .purchaseDate(collateral.getPurchaseDate())
                .quantity(collateral.getQuantity())
                .purchasingValue(collateral.getPurchasingValue())
                .estimatedValue(collateral.getEstimatedValue())
                .applicationNumber(collateral.getSalaryLoanApplication() != null ? collateral.getSalaryLoanApplication().getApplicationNumber() : null)
                .build();
    }


    private CollateralType parseCollateralType(String type) {
        try {
            return type != null ? CollateralType.valueOf(type.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid collateral type: " + type);
        }
    }


    private CollateralPurchaseCondition parsePurchaseCondition(String condition) {
        try {
            return condition != null ? CollateralPurchaseCondition.valueOf(condition.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid purchase condition: " + condition);
        }
    }

    private CollateralCondition parseCollateralCondition(String condition) {
        try {
            return condition != null ? CollateralCondition.valueOf(condition.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid collateral condition: " + condition);
        }
    }
}
