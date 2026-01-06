package com.machidior.Loan_Management_Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private String tinNumber;
    private String bankStatementUrl;
    private String insuranceComprehensiveCoverUrl;
    private String businessLicenseUrl;
    private String tinCertificateUrl;
    private String brelaCertificateUrl;
    private String cashFlowStatementUrl;
    private String ownershipType;
    private LocalDate startDate;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "businessDetails", cascade = CascadeType.ALL)
    private List<Business> businesses;

}
