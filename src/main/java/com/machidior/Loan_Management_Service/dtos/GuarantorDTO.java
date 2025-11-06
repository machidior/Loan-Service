package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuarantorDTO {
    private String name;
    private String relationship;
    private String phoneNumber;
    private String nationalId;
    private String address;
    private String document;
}
