package com.machidior.Loan_Management_Service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RepaymentScheduleItem {

    private int installmentNumber;
    private LocalDate dueDate;
    private BigDecimal paymentAmount;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal loanFee;
    private BigDecimal remainingBalance;


}
