package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.RepaymentScheduleItemDTO;
import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.model.RepaymentSchedule;
import com.machidior.Loan_Management_Service.repo.RepaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepaymentScheduleService {

    private final RepaymentScheduleRepository repository;

    public RepaymentSchedule getScheduleByLoanId(String loanId){
        return repository.findByLoanId(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no schedule with " + loanId + " loan id!"));
    }

    public List<RepaymentSchedule> getAllSchedules(){
        return repository.findAll();
    }

    public void deleteByLoanId(String loanId){
        RepaymentSchedule schedule = repository.findByLoanId(loanId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("No schedule with given loan id!")
                );
        repository.deleteById(schedule.getId());
    }

    public void deleteAllSchedules(){
        repository.deleteAll();
    }

    public List<RepaymentScheduleItemDTO> generateFlatSchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate,
            InstallmentFrequency frequency) {

        return switch (frequency) {
            case MONTHLY ->
                    generateFlatMonthlySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case WEEKLY ->
                    generateFlatWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case BIWEEKLY ->
                    generateFlatBiWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            default -> throw new IllegalArgumentException("Unsupported frequency");
        };
    }

    private List<RepaymentScheduleItemDTO> generateFlatMonthlySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItemDTO> schedule = new ArrayList<>();

        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal monthlyInterest = totalInterest.divide(BigDecimal.valueOf(termMonths));

        BigDecimal monthlyLoanFee = principal.multiply(loanFeeRate);

        BigDecimal principalPortion = principal.divide(BigDecimal.valueOf(termMonths));
        BigDecimal monthlyPayment = principalPortion.add(monthlyInterest).add(monthlyLoanFee);

        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusMonths(1); // first installment after disbursement

        for (int i = 1; i <= termMonths; i++) {

            BigDecimal principalPaid = principalPortion;
            if (principalPaid.compareTo(balance) > 0) principalPaid = balance;

            balance = balance.subtract(principalPaid);

            schedule.add(new RepaymentScheduleItemDTO(
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

    private List<RepaymentScheduleItemDTO> generateFlatWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItemDTO> schedule = new ArrayList<>();

        int totalWeeks = termMonths * 4;

        BigDecimal weeklyInterest = principal.multiply(monthlyRate)
                .multiply(BigDecimal.valueOf(termMonths))
                .divide(BigDecimal.valueOf(totalWeeks));

        BigDecimal weeklyLoanFee = principal.multiply(loanFeeRate)
                .divide(BigDecimal.valueOf(4));

        BigDecimal principalPortion = principal.divide(BigDecimal.valueOf(totalWeeks));
        BigDecimal weeklyPayment = principalPortion.add(weeklyInterest).add(weeklyLoanFee);

        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(1);

        for (int i = 1; i <= totalWeeks; i++) {

            BigDecimal principalPaid = principalPortion;
            if (principalPaid.compareTo(balance) > 0) principalPaid = balance;

            balance = balance.subtract(principalPaid);

            schedule.add(new RepaymentScheduleItemDTO(
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

    private List<RepaymentScheduleItemDTO> generateFlatBiWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItemDTO> schedule = new ArrayList<>();
        int totalPeriods = termMonths * 2;

        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal biWeeklyInterest = totalInterest.divide(BigDecimal.valueOf(totalPeriods));

        BigDecimal biWeeklyLoanFee = principal.multiply(loanFeeRate)
                .divide(BigDecimal.valueOf(2));

        BigDecimal principalPortion = principal.divide(BigDecimal.valueOf(totalPeriods));
        BigDecimal totalPayment = principalPortion.add(biWeeklyInterest).add(biWeeklyLoanFee);

        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(2);

        for (int i = 1; i <= totalPeriods; i++) {

            BigDecimal principalPaid = principalPortion;
            if (principalPaid.compareTo(balance) > 0) principalPaid = balance;

            balance = balance.subtract(principalPaid);

            schedule.add(new RepaymentScheduleItemDTO(
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

    public List<RepaymentScheduleItemDTO> generateReducingBalanceSchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate,
            InstallmentFrequency frequency) {

        return switch (frequency) {
            case MONTHLY ->
                    generateReducingMonthlySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case WEEKLY ->
                    generateReducingWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            case BIWEEKLY ->
                    generateReducingBiWeeklySchedule(principal, monthlyRate, loanFeeRate, termMonths, disbursementDate);
            default -> throw new IllegalArgumentException("Unsupported frequency");
        };
    }

    private List<RepaymentScheduleItemDTO> generateReducingMonthlySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItemDTO> schedule = new ArrayList<>();
        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusMonths(1);

        for (int i = 1; i <= termMonths; i++) {

            BigDecimal interest = balance.multiply(monthlyRate);

            BigDecimal loanFee = balance.multiply(loanFeeRate);

            BigDecimal principalPortion = balance.divide(BigDecimal.valueOf(termMonths - i + 1));

            BigDecimal totalPayment = principalPortion.add(interest).add(loanFee);

            balance = balance.subtract(principalPortion);

            schedule.add(new RepaymentScheduleItemDTO(
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

    private List<RepaymentScheduleItemDTO> generateReducingWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItemDTO> schedule = new ArrayList<>();
        int totalWeeks = termMonths * 4;
        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(1);

        for (int i = 1; i <= totalWeeks; i++) {

            BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4));
            BigDecimal interest = balance.multiply(weeklyRate);

            BigDecimal loanFee = balance.multiply(loanFeeRate)
                    .divide(BigDecimal.valueOf(4));

            BigDecimal principalPortion = balance.divide(BigDecimal.valueOf(totalWeeks - i + 1));

            BigDecimal totalPayment = principalPortion.add(interest).add(loanFee);

            balance = balance.subtract(principalPortion);

            schedule.add(new RepaymentScheduleItemDTO(
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

    private List<RepaymentScheduleItemDTO> generateReducingBiWeeklySchedule(
            BigDecimal principal,
            BigDecimal monthlyRate,
            BigDecimal loanFeeRate,
            int termMonths,
            LocalDate disbursementDate) {

        List<RepaymentScheduleItemDTO> schedule = new ArrayList<>();
        int totalPeriods = termMonths * 2;
        BigDecimal balance = principal;
        LocalDate dueDate = disbursementDate.plusWeeks(2);

        for (int i = 1; i <= totalPeriods; i++) {

            BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4));
            BigDecimal interest = balance.multiply(weeklyRate).multiply(BigDecimal.valueOf(2));

            BigDecimal loanFee = balance.multiply(loanFeeRate)
                    .divide(BigDecimal.valueOf(2));

            BigDecimal principalPortion = balance.divide(BigDecimal.valueOf(totalPeriods - i + 1));

            BigDecimal totalPayment = principalPortion.add(interest).add(loanFee);

            balance = balance.subtract(principalPortion);

            schedule.add(new RepaymentScheduleItemDTO(
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
