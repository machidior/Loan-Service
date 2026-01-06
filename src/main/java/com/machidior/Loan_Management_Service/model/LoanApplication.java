package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
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
    private String productName;

    @Column(nullable = false)
    private BigDecimal amountRequested;
    private BigDecimal amountApproved;
    private BigDecimal interestRate;
    @Column(name = "loan_fee_rate")
    private BigDecimal loanFeeRate;
    private Integer termMonths;
    @Enumerated(EnumType.STRING)
    private InstallmentFrequency installmentFrequency;
    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus status;

    private BigDecimal applicationFee;
    private BigDecimal loanInsuranceFee;

    private Boolean isRead;
    private String purpose;
    private String remarks;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Guarantor guarantor;

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL)
    private List<Collateral> collaterals;


    @OneToOne(cascade = CascadeType.ALL)
    private JobDetails jobDetails;

    @OneToOne(cascade = CascadeType.ALL)
    private BusinessDetails businessDetails;

}
