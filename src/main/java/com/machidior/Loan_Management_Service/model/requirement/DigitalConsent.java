package com.machidior.Loan_Management_Service.model.requirement;

import com.machidior.Loan_Management_Service.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "digital_consents")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DigitalConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean consentGiven;
    private LocalDateTime consentDate;

    private String ipAddress;
    private String deviceId;
    private String channel; // WEB, MOBILE

    private String consentDocumentUrl;

    private Boolean approved;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private LoanApplication loanApplication;
}
