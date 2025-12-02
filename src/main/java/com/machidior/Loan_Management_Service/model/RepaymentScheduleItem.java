package com.machidior.Loan_Management_Service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedule_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentScheduleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int installmentNumber;
    private LocalDate dueDate;
    private BigDecimal paymentAmount;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal loanFee;
    private BigDecimal remainingBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repayment_schedule_id", referencedColumnName = "id")
    @JsonIgnore
    private RepaymentSchedule repaymentSchedule;

}
