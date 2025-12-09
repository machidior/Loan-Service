package com.machidior.Loan_Management_Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    private String businessSector;
    private Integer yearsInOperation;
    private String tinNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_number", referencedColumnName = "applicationNumber")
    @JsonIgnore
    private BusinessLoanApplication businessLoanApplication;
}
