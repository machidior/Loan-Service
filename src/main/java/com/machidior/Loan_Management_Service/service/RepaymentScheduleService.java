package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.model.RepaymentScheduleItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepaymentScheduleService {

    private final LoanCalculationService calculator = new LoanCalculationService();

    public List<RepaymentScheduleItem> generateFlatSchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,      // interest rate per month
            BigDecimal loanFeeRate,      // loan fee per month (e.g., 0.065 for 6.5%)
            int termMonths,
            LocalDate disbursementDate,
            InstallmentFrequency frequency) {

        switch (frequency) {
            case MONTHLY:
                return generateFlatMonthlySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case WEEKLY:
                return generateFlatWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case BIWEEKLY:
                return generateFlatBiWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            default:
                throw new IllegalArgumentException("Unsupported frequency");
        }
    }

    private List<RepaymentScheduleItem> generateFlatMonthlySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItem> schedule = new ArrayList<>();

        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal monthlyInterest = totalInterest.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);

        BigDecimal monthlyLoanFee = principal.multiply(loanFeeRate).setScale(2, RoundingMode.HALF_UP);

        BigDecimal principalPortion = principal.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = principalPortion.add(monthlyInterest).add(monthlyLoanFee);

        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusMonths(1); // first installment after disbursement

        for (int i = 1; i <= termMonths; i++) {

            BigDecimal principalPaid = principalPortion;
            if (principalPaid.compareTo(balance) > 0) principalPaid = balance;

            balance = balance.subtract(principalPaid);

            schedule.add(new RepaymentScheduleItem(
                    i,
                    dueDate,
                    monthlyPayment,
                    principalPaid,
                    monthlyInterest,
                    monthlyLoanFee,
                    balance.max(BigDecimal.ZERO)
            ));

            dueDate = dueDate.plusMonths(1);
        }

        return schedule;
    }

    private List<RepaymentScheduleItem> generateFlatWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItem> schedule = new ArrayList<>();

        int totalWeeks = termMonths * 4;

        BigDecimal weeklyInterest = principal.multiply(monthlyRate)
                .multiply(BigDecimal.valueOf(termMonths))
                .divide(BigDecimal.valueOf(totalWeeks), 2, RoundingMode.HALF_UP);

        BigDecimal weeklyLoanFee = principal.multiply(loanFeeRate)
                .divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);

        BigDecimal principalPortion = principal.divide(BigDecimal.valueOf(totalWeeks), 2, RoundingMode.HALF_UP);
        BigDecimal weeklyPayment = principalPortion.add(weeklyInterest).add(weeklyLoanFee);

        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(1);

        for (int i = 1; i <= totalWeeks; i++) {

            BigDecimal principalPaid = principalPortion;
            if (principalPaid.compareTo(balance) > 0) principalPaid = balance;

            balance = balance.subtract(principalPaid);

            schedule.add(new RepaymentScheduleItem(
                    i,
                    dueDate,
                    weeklyPayment,
                    principalPaid,
                    weeklyInterest,
                    weeklyLoanFee,
                    balance.max(BigDecimal.ZERO)
            ));

            dueDate = dueDate.plusWeeks(1);
        }

        return schedule;
    }

    private List<RepaymentScheduleItem> generateFlatBiWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItem> schedule = new ArrayList<>();
        int totalPeriods = termMonths * 2; // 2 biweekly per month

        // Total interest on full principal
        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal biWeeklyInterest = totalInterest.divide(BigDecimal.valueOf(totalPeriods), 2, RoundingMode.HALF_UP);

        // Loan fee per period
        BigDecimal biWeeklyLoanFee = principal.multiply(loanFeeRate)
                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

        // Total payment per period
        BigDecimal principalPortion = principal.divide(BigDecimal.valueOf(totalPeriods), 2, RoundingMode.HALF_UP);
        BigDecimal totalPayment = principalPortion.add(biWeeklyInterest).add(biWeeklyLoanFee);

        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(2);

        for (int i = 1; i <= totalPeriods; i++) {

            BigDecimal principalPaid = principalPortion;
            if (principalPaid.compareTo(balance) > 0) principalPaid = balance;

            balance = balance.subtract(principalPaid);

            schedule.add(new RepaymentScheduleItem(
                    i,
                    dueDate,
                    totalPayment,
                    principalPaid,
                    biWeeklyInterest,
                    biWeeklyLoanFee,
                    balance.max(BigDecimal.ZERO)
            ));

            dueDate = dueDate.plusWeeks(2);
        }

        return schedule;
    }

    public List<RepaymentScheduleItem> generateReducingBalanceSchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,   // optional, like flat rate
            int termMonths,
            LocalDate disbursementDate,
            InstallmentFrequency frequency) {

        switch (frequency) {
            case MONTHLY:
                return generateReducingMonthlySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case WEEKLY:
                return generateReducingWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case BIWEEKLY:
                return generateReducingBiWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            default:
                throw new IllegalArgumentException("Unsupported frequency");
        }
    }

    private List<RepaymentScheduleItem> generateReducingMonthlySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItem> schedule = new ArrayList<>();
        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusMonths(1);

        for (int i = 1; i <= termMonths; i++) {

            // Interest on remaining balance
            BigDecimal interest = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);

            // Loan fee (optional)
            BigDecimal loanFee = balance.multiply(loanFeeRate).setScale(2, RoundingMode.HALF_UP);

            // Principal portion = fixed principal / remaining periods
            BigDecimal principalPortion = balance.divide(BigDecimal.valueOf(termMonths - i + 1), 2, RoundingMode.HALF_UP);

            BigDecimal totalPayment = principalPortion.add(interest).add(loanFee);

            balance = balance.subtract(principalPortion);

            schedule.add(new RepaymentScheduleItem(
                    i,
                    dueDate,
                    totalPayment,
                    principalPortion,
                    interest,
                    loanFee,
                    balance.max(BigDecimal.ZERO)
            ));

            dueDate = dueDate.plusMonths(1);
        }

        return schedule;
    }

    private List<RepaymentScheduleItem> generateReducingWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItem> schedule = new ArrayList<>();
        int totalWeeks = termMonths * 4;
        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(1);

        for (int i = 1; i <= totalWeeks; i++) {

            BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4), 8, RoundingMode.HALF_UP);
            BigDecimal interest = balance.multiply(weeklyRate).setScale(2, RoundingMode.HALF_UP);

            BigDecimal loanFee = balance.multiply(loanFeeRate)
                    .divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);

            BigDecimal principalPortion = balance.divide(BigDecimal.valueOf(totalWeeks - i + 1), 2, RoundingMode.HALF_UP);

            BigDecimal totalPayment = principalPortion.add(interest).add(loanFee);

            balance = balance.subtract(principalPortion);

            schedule.add(new RepaymentScheduleItem(
                    i,
                    dueDate,
                    totalPayment,
                    principalPortion,
                    interest,
                    loanFee,
                    balance.max(BigDecimal.ZERO)
            ));

            dueDate = dueDate.plusWeeks(1);
        }

        return schedule;
    }

    private List<RepaymentScheduleItem> generateReducingBiWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItem> schedule = new ArrayList<>();
        int totalPeriods = termMonths * 2; // 2 biweekly per month
        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(2);

        for (int i = 1; i <= totalPeriods; i++) {

            BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4), 8, RoundingMode.HALF_UP);
            BigDecimal interest = balance.multiply(weeklyRate).multiply(BigDecimal.valueOf(2)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal loanFee = balance.multiply(loanFeeRate)
                    .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP); // 2 weeks

            BigDecimal principalPortion = balance.divide(BigDecimal.valueOf(totalPeriods - i + 1), 2, RoundingMode.HALF_UP);

            BigDecimal totalPayment = principalPortion.add(interest).add(loanFee);

            balance = balance.subtract(principalPortion);

            schedule.add(new RepaymentScheduleItem(
                    i,
                    dueDate,
                    totalPayment,
                    principalPortion,
                    interest,
                    loanFee,
                    balance.max(BigDecimal.ZERO)
            ));

            dueDate = dueDate.plusWeeks(2);
        }

        return schedule;
    }

}
