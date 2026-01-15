package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.request.BusinessDetailsRequest;
import com.machidior.Loan_Management_Service.dtos.response.BusinessDetailsResponse;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.model.requirement.BusinessDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusinessDetailsMapper {

    private final BusinessMapper businessMapper;

    public BusinessDetails toEntity(BusinessDetailsRequest request, LoanApplication application) {
        BusinessDetails businessDetails = new BusinessDetails();
        businessDetails.setTinNumber(request.getTinNumber());
        businessDetails.setAverageMonthlyTurnover(request.getAverageMonthlyTurnover());
        businessDetails.setBusinesses(
                request.getBusinessList() != null ?
                        request.getBusinessList().stream()
                                .map(business -> businessMapper.toEntity(business, businessDetails))
                                .collect(Collectors.toList()) : null
        );
        businessDetails.setLoanApplication(application);
        return businessDetails;
    }

    public BusinessDetailsResponse toResponse(BusinessDetails businessDetails) {

        return BusinessDetailsResponse.builder()
                .id(businessDetails.getId())
                .tinNumber(businessDetails.getTinNumber())
                .averageMonthlyTurnOver(businessDetails.getAverageMonthlyTurnover())
                .insuranceComprehensiveCoverUrl(businessDetails.getInsuranceComprehensiveCoverUrl())
                .businessLicenseUrl(businessDetails.getBusinessLicenseUrl())
                .tinCertificateUrl(businessDetails.getTinCertificateUrl())
                .brelaCertificateUrl(businessDetails.getBrelaCertificateUrl())
                .cashFlowStatementUrl(businessDetails.getCashFlowStatementUrl())
                .businesses(businessDetails.getBusinesses() != null ?
                        businessDetails.getBusinesses().stream()
                                .map(businessMapper::toResponse)
                                .collect(Collectors.toList()) : null)
                .createdAt(businessDetails.getCreatedAt())
                .updatedAt(businessDetails.getUpdatedAt())
                .build();
    }

}

