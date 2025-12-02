package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_loan_guarantors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoanGuarantor {

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
    private String age;
    private String address;
    private String guarantee;
    private BigDecimal GuaranteeValue;

    private boolean approved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_number")
    private SalaryLoanApplication salaryLoanApplication;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
