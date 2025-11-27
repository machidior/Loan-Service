package com.machidior.Loan_Management_Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDetails {

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
    @JoinColumn(name = "business_loan_id", referencedColumnName = "id")
    @JsonIgnore
    private BusinessLoan businessLoan;
}
