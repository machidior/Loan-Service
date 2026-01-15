package com.machidior.Loan_Management_Service.model.requirement;

import com.machidior.Loan_Management_Service.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "education_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String highestEducationLevel;
    private String institutionName;
    private String fieldOfStudy;
    private Integer completionYear;

    private Boolean admissionLetterProvided;
    private String admissionLetterUrl;

    private Boolean feeStructureProvided;
    private String feeStructureUrl;

    private Boolean sponsorProvided;
    private String sponsorDetails;

    private Boolean guarantorProvided;
    private String guarantorDetails;

    private Boolean relatedEducationCertificateProvided;
    private String relatedEducationCertificateUrl;

    private Boolean approved;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    private LoanApplication loanApplication;

}

