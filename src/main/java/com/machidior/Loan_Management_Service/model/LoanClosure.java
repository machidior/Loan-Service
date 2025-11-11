package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_closures")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanClosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String closedBy;
    private String loanId;
    @CreationTimestamp
    private LocalDateTime closedAt;
    private String remarks;

}
