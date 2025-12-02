package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "guarantors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class   BusinessLoanGuarantor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String relationship;
    private String phoneNumber;
    private String nationalId;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    private String occupation;
    private String age;
    private String address;
    private String guarantee;
    private BigDecimal GuaranteeValue;

    private boolean approved;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "loan_application_number")
    private BusinessLoanApplication businessLoanApplication;
}
