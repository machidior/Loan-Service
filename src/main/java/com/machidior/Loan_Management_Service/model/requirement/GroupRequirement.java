package com.machidior.Loan_Management_Service.model.requirement;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    private String groupRegistrationNumber;

    private String groupRegistrationDocumentUrl;

    private Integer totalGroupMembers;

    private List<String> groupMemberList;

    private String groupGuaranteeAgreementUrl;

    private Integer meetingMinutes;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
