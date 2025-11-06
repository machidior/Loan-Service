package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guarantors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Guarantor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String relationship;
    private String phoneNumber;
    private String nationalId;
    private String address;
    private String document;//scanned ID or signature file (String for now)

    private boolean approved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id")
    private LoanApplication loanApplication;
}
