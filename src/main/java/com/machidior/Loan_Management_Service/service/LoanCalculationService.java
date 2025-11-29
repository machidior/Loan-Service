package com.machidior.Loan_Management_Service.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoanCalculationService {

    public BigDecimal calculateFlatMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal totalPayable = principal.add(totalInterest);

        return totalPayable.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateFlatWeeklyPayment(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal totalPayable = principal.add(totalInterest);

        int totalWeeks = termMonths * 4;

        return totalPayable.divide(BigDecimal.valueOf(totalWeeks), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMonthlyInterest(BigDecimal balance, BigDecimal monthlyRate) {
        return balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateWeeklyInterest(BigDecimal balance, BigDecimal monthlyRate) {
        BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4), 8, RoundingMode.HALF_UP);
        return balance.multiply(weeklyRate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateLoanFee(BigDecimal totalInterestRate, BigDecimal interestRate){
        return totalInterestRate.subtract(interestRate);
    }







    //public BigDecimal calculateMonthlyEMI(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
//        BigDecimal one = BigDecimal.ONE;
//
//        BigDecimal numerator = principal
//                .multiply(monthlyRate)
//                .multiply((one.add(monthlyRate)).pow(termMonths));
//
//        BigDecimal denominator = (one.add(monthlyRate)).pow(termMonths).subtract(one);
//
//        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
//    }
//
//    public BigDecimal calculateWeeklyEMI(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
//        BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4), 8, RoundingMode.HALF_UP);
//        int totalWeeks = termMonths * 4;
//
//        BigDecimal one = BigDecimal.ONE;
//
//        BigDecimal numerator = principal
//                .multiply(weeklyRate)
//                .multiply((one.add(weeklyRate)).pow(totalWeeks));
//
//        BigDecimal denominator = (one.add(weeklyRate)).pow(totalWeeks).subtract(one);
//
//        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
//    }


}
