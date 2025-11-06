package com.machidior.Loan_Management_Service.generator;


import com.machidior.Loan_Management_Service.enums.RepaymentStatus;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.model.RepaymentSchedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepaymentScheduleGenerator {

    public static List<RepaymentSchedule> generate(LoanApplication loan) {
        List<RepaymentSchedule> schedules = new ArrayList<>();
        BigDecimal monthlyPrincipal = loan.getAmountApproved()
                .divide(BigDecimal.valueOf(loan.getTermMonths()), BigDecimal.ROUND_HALF_UP);

        BigDecimal monthlyInterest = loan.getAmountApproved()
                .multiply(loan.getInterestRate().divide(BigDecimal.valueOf(100)))
                .divide(BigDecimal.valueOf(loan.getTermMonths()), BigDecimal.ROUND_HALF_UP);

        LocalDate start = LocalDate.now().plusMonths(1);

        for (int i = 1; i <= loan.getTermMonths(); i++) {
            RepaymentSchedule schedule = new RepaymentSchedule();
            schedule.setInstallmentNumber(i);
            schedule.setDueDate(start.plusMonths(i - 1));
            schedule.setPrincipalDue(monthlyPrincipal);
            schedule.setInterestDue(monthlyInterest);
            schedule.setTotalDue(monthlyPrincipal.add(monthlyInterest));
            schedule.setStatus(RepaymentStatus.PENDING);
            schedule.setLoanApplication(loan);
            schedules.add(schedule);
        }

        return schedules;
    }
}
