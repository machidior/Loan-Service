package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "kuza_loan_guarantor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KuzaLoanGuarantor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String relationship;
    private String phoneNumber;
    private String nationalId;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    private String occupation;
    private Integer age;
    private String address;
    private String guarantee;
    private BigDecimal guaranteeValue;

    private String passportUrl;
    private String identificationCardUrl;

    private boolean approved;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_number")
    private KuzaLoanApplication kuzaLoanApplication;
}
