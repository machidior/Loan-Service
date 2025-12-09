package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "business_loan_applications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessLoanApplication {


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
    @Enumerated(EnumType.STRING)
    private LoanProductType productType;

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

    private String bankStatementUrl;
    private String insuranceComprehensiveCoverUrl;
    private String businessLicenseUrl;
    private String tinCertificateUrl;
    private String brelaCertificateUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "businessLoanApplication", cascade = CascadeType.ALL)
    private List<BusinessDetails> businessDetails;
    @OneToOne(mappedBy = "businessLoanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private BusinessLoanGuarantor guarantor;
    @OneToMany(mappedBy = "businessLoanApplication", cascade = CascadeType.ALL)
    private List<BusinessLoanCollateral> collaterals;

}
