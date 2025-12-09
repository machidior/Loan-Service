package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralResponse;
import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.BusinessLoanApplication;
import com.machidior.Loan_Management_Service.model.BusinessLoanCollateral;
import org.springframework.stereotype.Component;

@Component
public class BusinessLoanCollateralMapper {


    public BusinessLoanCollateral toEntity(BusinessLoanCollateralRequest request, BusinessLoanApplication businessLoanApplication) {
        return BusinessLoanCollateral.builder()
                .type(request.getType())
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .purchaseCondition(request.getPurchaseCondition())
                .condition(request.getCondition())
                .purchaseDate(request.getPurchaseDate())
                .quantity(request.getQuantity())
                .purchasingValue(request.getPurchasingValue())
                .estimatedValue(request.getEstimatedValue())
                .businessLoanApplication(businessLoanApplication)
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
                .condition(collateral.getCondition())
                .photoUrl(collateral.getPhotoUrl())
                .purchaseDate(collateral.getPurchaseDate())
                .quantity(collateral.getQuantity())
                .purchasingValue(collateral.getPurchasingValue())
                .estimatedValue(collateral.getEstimatedValue())
                .applicationNumber(collateral.getBusinessLoanApplication() != null ? collateral.getBusinessLoanApplication().getApplicationNumber(): null)
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
