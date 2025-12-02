package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanGuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanGuarantorResponse;
import com.machidior.Loan_Management_Service.enums.Gender;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.BusinessLoanApplication;
import com.machidior.Loan_Management_Service.model.BusinessLoanGuarantor;
import org.springframework.stereotype.Component;

@Component
public class BusinessLoanGuarantorMapper {


    public BusinessLoanGuarantor toEntity(BusinessLoanGuarantorRequest request, BusinessLoanApplication businessLoan) {
        return BusinessLoanGuarantor.builder()
                .name(request.getName())
                .relationship(request.getRelationship())
                .phoneNumber(request.getPhoneNumber())
                .nationalId(request.getNationalId())
                .gender(parseGender(request.getGender()))
                .email(request.getEmail())
                .occupation(request.getOccupation())
                .age(request.getAge())
                .address(request.getAddress())
                .guarantee(request.getGuarantee())
                .GuaranteeValue(request.getGuaranteeValue())
                .approved(false)
                .businessLoanApplication(businessLoan)
                .build();
    }


    public BusinessLoanGuarantorResponse toResponse(BusinessLoanGuarantor guarantor) {
        return BusinessLoanGuarantorResponse.builder()
                .id(guarantor.getId())
                .name(guarantor.getName())
                .relationship(guarantor.getRelationship())
                .phoneNumber(guarantor.getPhoneNumber())
                .nationalId(guarantor.getNationalId())
                .gender(guarantor.getGender())
                .email(guarantor.getEmail())
                .occupation(guarantor.getOccupation())
                .age(guarantor.getAge())
                .address(guarantor.getAddress())
                .guarantee(guarantor.getGuarantee())
                .GuaranteeValue(guarantor.getGuaranteeValue())
                .approved(guarantor.isApproved())
                .businessLoanId(
                        guarantor.getBusinessLoanApplication() != null
                                ? guarantor.getBusinessLoanApplication().getApplicationNumber()
                                : null
                )
                .build();
    }


    private Gender parseGender(String gender) {
        try {
            return gender != null ? Gender.valueOf(gender.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender value: " + gender);
        }
    }
}
