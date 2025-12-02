package com.machidior.Loan_Management_Service.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentScheduleItemDTO {
    private int installmentNumber;
    private LocalDate dueDate;
    private BigDecimal paymentAmount;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal loanFee;
    private BigDecimal remainingBalance;
}
