package com.machidior.Loan_Management_Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kuza_loan_business_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KuzaBusinessDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String businessName;
    private String businessType;
    private String businessAddress;
    private Integer yearsInOperation;
    private String bankStatement;
    private String insuranceComprehensiveCover;
    private String businessLicense;
    private String tinCertificate;
    private String tinNumber;
    private String brelaCertificate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_number", referencedColumnName = "applicationNumber")
    @JsonIgnore
    private KuzaLoanApplication kuzaLoanApplication;
}
