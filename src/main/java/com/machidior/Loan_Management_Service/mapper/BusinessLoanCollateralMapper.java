package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralResponse;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.BusinessLoanCollateral;
import org.springframework.stereotype.Component;

@Component
public class BusinessLoanCollateralMapper {


    public BusinessLoanCollateral toEntity(BusinessLoanCollateralRequest request, BusinessLoan businessLoan) {
        return BusinessLoanCollateral.builder()
                .type(parseCollateralType(request.getType()))
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .purchaseCondition(parsePurchaseCondition(request.getPurchaseCondition()))
                .photo(request.getPhoto())
                .purchaseDate(request.getPurchaseDate())
                .quantity(request.getQuantity())
                .purchasingValue(request.getPurchasingValue())
                .estimatedValue(request.getEstimatedValue())
                .businessLoan(businessLoan)
                .build();
    }

    public BusinessLoanCollateralResponse toResponse(BusinessLoanCollateral collateral) {
        return BusinessLoanCollateralResponse.builder()
                .id(collateral.getId())
                .CustomerId(collateral.getCustomerId())
                .type(collateral.getType())
                .name(collateral.getName())
                .description(collateral.getDescription())
                .location(collateral.getLocation())
                .purchaseCondition(collateral.getPurchaseCondition())
                .photo(collateral.getPhoto())
                .purchaseDate(collateral.getPurchaseDate())
                .quantity(collateral.getQuantity())
                .purchasingValue(collateral.getPurchasingValue())
                .estimatedValue(collateral.getEstimatedValue())
                .businessLoanId(collateral.getBusinessLoan() != null ? collateral.getBusinessLoan().getId() : null)
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
}
