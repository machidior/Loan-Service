package com.machidior.Loan_Management_Service.completion;

import com.machidior.Loan_Management_Service.enums.RequirementStatus;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"application_number", "requirementType"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationRequirementStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String applicationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequirementType requirementType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequirementStatus status;
}

