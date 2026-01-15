package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.TenureUnit;
import com.machidior.Loan_Management_Service.model.requirement.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanApplication {


    @Id
    @GeneratedValue(generator = "application_number_generator")
    @GenericGenerator(
            name = "application_number_generator",
            strategy = "com.machidior.Loan_Management_Service.generator.ApplicationNumberGenerator"
    )
    private String applicationNumber;

    @Column(nullable = false)
    private String customerId;
    private String loanOfficerId;
    private Long productId;
    private Long productVersionId;
    private String productName;
    private String productCode;

    @Column(nullable = false)
    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    private Integer termTenure;
    private TenureUnit tenureUnit;
    @Enumerated(EnumType.STRING)
    private InstallmentFrequency installmentFrequency;
    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus status;

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL)
    private List<OnApplicationCharge> charges;

    private Boolean isRead;
    private String purpose;
    private String remarks;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


}
