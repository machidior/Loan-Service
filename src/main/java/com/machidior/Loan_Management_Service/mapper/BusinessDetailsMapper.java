package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.BusinessDetailsRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessDetailsResponse;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusinessDetailsMapper {

    private final BusinessMapper businessMapper;

    public BusinessDetails toEntity(BusinessDetailsRequest request) {
        BusinessDetails businessDetails = new BusinessDetails();
        businessDetails.setTinNumber(request.getTinNumber());
        businessDetails.setBusinesses(
                request.getBusinessList() != null ?
                        request.getBusinessList().stream()
                                .map(business -> businessMapper.toEntity(business, businessDetails))
                                .collect(Collectors.toList()) : null
        );

        return businessDetails;
    }

    public BusinessDetailsResponse toResponse(BusinessDetails businessDetails) {

        return BusinessDetailsResponse.builder()
                .id(businessDetails.getId())
                .tinNumber(businessDetails.getTinNumber())
                .bankStatementUrl(businessDetails.getBankStatementUrl())
                .insuranceComprehensiveCoverUrl(businessDetails.getInsuranceComprehensiveCoverUrl())
                .businessLicenseUrl(businessDetails.getBusinessLicenseUrl())
                .tinCertificateUrl(businessDetails.getTinCertificateUrl())
                .brelaCertificateUrl(businessDetails.getBrelaCertificateUrl())
                .businesses(businessDetails.getBusinesses() != null ?
                        businessDetails.getBusinesses().stream()
                                .map(businessMapper::toResponse)
                                .collect(Collectors.toList()) : null)
                .createdAt(businessDetails.getCreatedAt())
                .updatedAt(businessDetails.getUpdatedAt())
                .build();
    }
}

