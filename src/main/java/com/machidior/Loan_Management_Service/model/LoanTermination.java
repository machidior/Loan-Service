package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "loan_terminations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanTermination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    private String terminatedBy;
    @CreationTimestamp
    private String terminationAt;
    private String terminationReason;
}
