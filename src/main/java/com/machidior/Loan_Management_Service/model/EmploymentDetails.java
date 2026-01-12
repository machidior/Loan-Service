package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmploymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String companyLocation;
    private String role;

    private String bankStatementUrl;
    private String salarySlipUrl;
    private String insuranceComprehensiveCoverUrl;
    private String jobContractUrl;
    private LocalDate employmentStartDate;
    private BigDecimal netMonthlySalary;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
