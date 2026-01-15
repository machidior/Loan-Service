package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.request.GuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.response.GuarantorResponse;
import com.machidior.Loan_Management_Service.enums.Gender;
import com.machidior.Loan_Management_Service.model.requirement.Guarantor;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import org.springframework.stereotype.Component;

@Component
public class GuarantorMapper {

    public Guarantor toEntity(GuarantorRequest request, LoanApplication application) {
        return Guarantor.builder()
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
                .guaranteeValue(request.getGuaranteeValue())
                .approved(false)
                .loanApplication(application)
                .build();
    }

    public GuarantorResponse toResponse(Guarantor guarantor) {
        return GuarantorResponse.builder()
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
                .passportUrl(guarantor.getPassportUrl())
                .identificationCardUrl(guarantor.getIdentificationCardUrl())
                .guarantorConsentUrl(guarantor.getGuarantorConsentUrl())
                .incomeProofUrl(guarantor.getIncomeProofUrl())
                .guarantee(guarantor.getGuarantee())
                .guaranteeValue(guarantor.getGuaranteeValue())
                .approved(guarantor.isApproved())
                .applicationNumber(
                        guarantor.getLoanApplication() != null
                                ? guarantor.getLoanApplication().getApplicationNumber()
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
