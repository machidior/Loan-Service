package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.enums.TenureUnit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(generator = "loan_id_generator")
    @GenericGenerator(
            name = "loan_id_generator",
            strategy = "com.machidior.Loan_Management_Service.generator.LoanIdGenerator"
    )
    private String id;
    private String applicationNumber;
    private Long productId;
    private String productName;
    private String customerId;
    private BigDecimal principal;
    private BigDecimal interestRate;
    @Enumerated(EnumType.STRING)
    private InstallmentFrequency installmentFrequency;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    private BigDecimal totalPayableAmount;
    private Integer termTenure;
    private TenureUnit tenureUnit;
    private String loanContractUrl;
    private LocalDate disbursedOn;
    private LocalDateTime approvedOn;
    private LocalDateTime appliedOn;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
