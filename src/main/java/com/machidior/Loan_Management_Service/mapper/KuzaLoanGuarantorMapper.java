package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.KuzaLoanGuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.KuzaLoanGuarantorResponse;
import com.machidior.Loan_Management_Service.enums.Gender;
import com.machidior.Loan_Management_Service.model.KuzaLoanApplication;
import com.machidior.Loan_Management_Service.model.KuzaLoanGuarantor;
import org.springframework.stereotype.Component;

@Component
public class KuzaLoanGuarantorMapper {

    public KuzaLoanGuarantor toEntity(KuzaLoanGuarantorRequest request, KuzaLoanApplication kuzaLoanApplication) {
        return KuzaLoanGuarantor.builder()
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
                .kuzaLoanApplication(kuzaLoanApplication)
                .build();
    }


    public KuzaLoanGuarantorResponse toResponse(KuzaLoanGuarantor guarantor) {
        return KuzaLoanGuarantorResponse.builder()
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
                .applicationNumber(
                        guarantor.getKuzaLoanApplication() != null
                                ? guarantor.getKuzaLoanApplication().getApplicationNumber()
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
