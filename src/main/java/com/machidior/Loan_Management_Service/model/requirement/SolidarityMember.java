package com.machidior.Loan_Management_Service.model.requirement;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//ToDo: This will be implemented during the solidarity group lending feature development
@Entity
@Table(name = "solidarity_members")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolidarityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberName;
    private String nationalId;
    private String phoneNumber;
    private String role; // LEADER, MEMBER

    private Boolean consentProvided;
    private String consentDocumentUrl;

//    ToDo: Add relationship to LoanApplication when the entity is linked to it
    private Boolean groupRegistrationProvided;
    private Boolean groupGuaranteeProvided;
    private Boolean groupMeetingRecordsProvided;

    private BigDecimal contributionAmount;

    private Boolean approved;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}

