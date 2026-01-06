package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.BusinessRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessResponse;
import com.machidior.Loan_Management_Service.model.Business;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {

    public Business toEntity(BusinessRequest request, BusinessDetails businessDetails) {

        return Business.builder()
                .businessName(request.getBusinessName())
                .businessType(request.getBusinessType())
                .businessAddress(request.getBusinessAddress())
                .businessSector(request.getBusinessSector())
                .yearsInOperation(request.getYearsInOperation())
                .businessDetails(businessDetails)
                .build();
    }

    public BusinessResponse toResponse(Business business) {

        return BusinessResponse.builder()
                .businessName(business.getBusinessName())
                .businessType(business.getBusinessType())
                .businessAddress(business.getBusinessAddress())
                .businessSector(business.getBusinessSector())
                .yearsInOperation(business.getYearsInOperation())
                .businessDetailsId(business.getBusinessDetails().getId())
                .build();
    }
}
