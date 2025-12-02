package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.CollateralCondition;
import com.machidior.Loan_Management_Service.enums.CollateralPurchaseCondition;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_loan_collaterals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryLoanCollateral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CustomerId;
    @Enumerated(EnumType.STRING)
    private CollateralType type;
    private String name;
    private String description;
    private String location;
    @Enumerated(EnumType.STRING)
    private CollateralPurchaseCondition purchaseCondition;
    private String photo;
    @Enumerated(EnumType.STRING)
    private CollateralCondition condition;
    private LocalDate purchaseDate;
    private Integer quantity;
    @Column(name = "purchasing_value")
    private BigDecimal purchasingValue;
    @Column(name = "estimated_value")
    private BigDecimal estimatedValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_number")
    private SalaryLoanApplication salaryLoanApplication;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
