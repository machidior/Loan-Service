package com.machidior.Loan_Management_Service.model.requirement;

import com.machidior.Loan_Management_Service.enums.EmploymentType;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmploymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employerName;
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;
    private String employerAddress;
    private String employerContactNumber;
    private String jobTitle;

    private BigDecimal monthlyIncome;
    private BigDecimal netMonthlyIncome;

    private Boolean payslipProvided;
    private String paySlipUrl;

    private Boolean insuranceComprehensiveCoverProvided;
    private String insuranceComprehensiveCoverUrl;

    private Boolean contractProvided;
    private String contractUrl;

    private LocalDate employmentStartDate;
    private Integer employmentDurationMonths;

    private Boolean payrollDeductionProvided;
    private String payrollDeductionUrl;

    private Boolean approved;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    private LoanApplication loanApplication;
}
