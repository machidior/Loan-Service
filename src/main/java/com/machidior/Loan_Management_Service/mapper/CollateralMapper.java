package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.CollateralRequest;
import com.machidior.Loan_Management_Service.dtos.CollateralResponse;
import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import com.machidior.Loan_Management_Service.model.Collateral;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import org.springframework.stereotype.Component;

@Component
public class CollateralMapper {

    public Collateral toEntity(CollateralRequest request, LoanApplication application) {
        return Collateral.builder()
                .type(parseCollateralType(request.getType()))
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .purchaseCondition(parsePurchaseCondition(request.getPurchaseCondition()))
                .condition(parseCollateralCondition(request.getCondition()))
                .purchaseDate(request.getPurchaseDate())
                .quantity(request.getQuantity())
                .purchasingValue(request.getPurchasingValue())
                .estimatedValue(request.getEstimatedValue())
                .loanApplication(application)
                .build();
    }

    public CollateralResponse toResponse(Collateral collateral) {
        return CollateralResponse.builder()
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
                .applicationNumber(collateral.getLoanApplication() != null ? collateral.getLoanApplication().getApplicationNumber(): null)
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
