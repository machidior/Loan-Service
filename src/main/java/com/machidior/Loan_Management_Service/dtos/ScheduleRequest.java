package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
public class ScheduleRequest {
    private BigDecimal principal;
    private BigDecimal monthlyRate;
    private BigDecimal loanFeeRate;
    private int termMonths;
    private LocalDate disbursementDate;
    private InstallmentFrequency frequency;
}
