package com.machidior.Loan_Management_Service.model.requirement;

import com.machidior.Loan_Management_Service.model.LoanApplication;
import jakarta.persistence.*;
import lombok.*;
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
public class FinancialHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<String> bankStatementsUrls;

    private List<String> mobileMoneyStatementsUrls;

    private String declaredMonthlyExpensesUrl;
    private BigDecimal declaredMonthlyIncome;

    private Boolean approved;



    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    private LoanApplication loanApplication;
}
