package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_event_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType; // e.g. CREATED, APPROVED, DISBURSED, REJECTED
    private String payload;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String createdBy;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "loan_application_id")
//    private LoanApplication loanApplication;
}
