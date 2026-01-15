package com.machidior.Loan_Management_Service.evaluator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationRequirementData {
    private Boolean admissionLetterProvided;
    private Boolean feeStructureProvided;
    private Boolean sponsorProvided;
    private Boolean guarantorProvided;
    private Boolean relatedEducationCertificateProvided;
}
