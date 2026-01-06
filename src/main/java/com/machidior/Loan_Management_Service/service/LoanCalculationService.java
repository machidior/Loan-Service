package com.machidior.Loan_Management_Service.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanCalculationService {

    public BigDecimal calculateFlatMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal totalPayable = principal.add(totalInterest);

        return totalPayable.divide(BigDecimal.valueOf(termMonths));
    }

    public BigDecimal calculateFlatWeeklyPayment(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        BigDecimal totalInterest = principal.multiply(monthlyRate).multiply(BigDecimal.valueOf(termMonths));
        BigDecimal totalPayable = principal.add(totalInterest);

        int totalWeeks = termMonths * 4;

        return totalPayable.divide(BigDecimal.valueOf(totalWeeks));
    }

    public BigDecimal calculateMonthlyInterest(BigDecimal balance, BigDecimal monthlyRate) {
        return balance.multiply(monthlyRate);
    }

    public BigDecimal calculateWeeklyInterest(BigDecimal balance, BigDecimal monthlyRate) {
        BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4));
        return balance.multiply(weeklyRate);
    }

    public BigDecimal calculateLoanFee(BigDecimal totalInterestRate, BigDecimal interestRate){
        return totalInterestRate.subtract(interestRate);
    }

    public BigDecimal rateToDecimal(BigDecimal rate){
        return rate.divide(BigDecimal.valueOf(100));
    }

    public BigDecimal rateToValue(BigDecimal rate, BigDecimal principal){
        return principal.multiply(rateToDecimal(rate));
    }

    public BigDecimal totalInterest(BigDecimal interestRate, BigDecimal principal, int termMonths){
        return rateToValue(interestRate, principal).multiply(BigDecimal.valueOf(termMonths));
    }

    public BigDecimal totalLoanFee(BigDecimal loanFeeRate, BigDecimal principal, int termMonths){
        return rateToValue(loanFeeRate, principal).multiply(BigDecimal.valueOf(termMonths));
    }

    public BigDecimal totalPayableAmount(BigDecimal principal, BigDecimal totalLoanFee, BigDecimal totalInterestRate){
        return principal.add(totalInterestRate).add(totalLoanFee);
    }













    public BigDecimal calculateMonthlyEMI(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        BigDecimal one = BigDecimal.ONE;

        BigDecimal numerator = principal
                .multiply(monthlyRate)
                .multiply((one.add(monthlyRate)).pow(termMonths));

        BigDecimal denominator = (one.add(monthlyRate)).pow(termMonths).subtract(one);

        return numerator.divide(denominator);
    }

    public BigDecimal calculateWeeklyEMI(BigDecimal principal, BigDecimal monthlyRate, int termMonths) {
        BigDecimal weeklyRate = monthlyRate.divide(BigDecimal.valueOf(4));
        int totalWeeks = termMonths * 4;

        BigDecimal one = BigDecimal.ONE;

        BigDecimal numerator = principal
                .multiply(weeklyRate)
                .multiply((one.add(weeklyRate)).pow(totalWeeks));

        BigDecimal denominator = (one.add(weeklyRate)).pow(totalWeeks).subtract(one);

        return numerator.divide(denominator);
    }


}
