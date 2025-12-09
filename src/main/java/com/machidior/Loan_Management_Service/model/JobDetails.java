package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDetails {

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

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
