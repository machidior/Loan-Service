package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_rejections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRejection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    private String rejectedBy;
    @CreationTimestamp
    private LocalDateTime rejectedAt;
    private String rejectionReason;
}
