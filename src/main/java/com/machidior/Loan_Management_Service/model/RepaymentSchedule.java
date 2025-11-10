package com.machidior.Loan_Management_Service.model;

import com.machidior.Loan_Management_Service.enums.RepaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer installmentNumber;
    private LocalDate dueDate;

    private BigDecimal principalDue;
    private BigDecimal interestDue;
    private BigDecimal totalDue;

    private BigDecimal paidAmount;
    private LocalDate paidAt;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus status;


}
