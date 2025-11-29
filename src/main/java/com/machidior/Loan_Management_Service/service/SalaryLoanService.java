package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.SalaryLoanRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanResponse;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.generator.ApplicationNumberGenerator;
import com.machidior.Loan_Management_Service.mapper.SalaryLoanMapper;
import com.machidior.Loan_Management_Service.model.LoanProductCharges;
import com.machidior.Loan_Management_Service.model.LoanProductTerms;
import com.machidior.Loan_Management_Service.model.SalaryLoan;
import com.machidior.Loan_Management_Service.repo.LoanProductChargesRepository;
import com.machidior.Loan_Management_Service.repo.LoanProductTermsRepository;
import com.machidior.Loan_Management_Service.repo.SalaryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaryLoanService {

    private final SalaryLoanRepository salaryLoanRepository;
    private final LoanProductTermsRepository termsRepository;
    private final LoanProductChargesRepository chargesRepository;
    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    public SalaryLoanResponse applyForLoan(SalaryLoanRequest request) {

        LoanProductTerms terms = termsRepository.findByProductType(LoanProductType.SALARY_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan terms not defined for Salary Product"));

        BigDecimal requestedAmount = request.getAmountRequested();
        if (requestedAmount.compareTo(terms.getMinAmount()) < 0 ||
                requestedAmount.compareTo(terms.getMaxAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Requested amount is outside the allowed range: " +
                            terms.getMinAmount() + " - " + terms.getMaxAmount());
        }

        LoanProductCharges charges = chargesRepository.findByProductType(LoanProductType.SALARY_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Charges not defined for Salary Product"));

        BigDecimal interestRate = terms.getMonthlyInterestRate();

        BigDecimal insurance = requestedAmount
                .multiply(charges.getLoanInsurancePercent()
                        .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));

        boolean isFirstLoan = isFirstLoanForCustomer(request.getCustomerId());
        BigDecimal applicationFee = isFirstLoan
                ? charges.getFirstApplicationFee()
                : charges.getSubsequentApplicationFee();

        BigDecimal totalInterest = requestedAmount
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);

        BigDecimal totalPayable = requestedAmount
                .add(totalInterest)
                .add(applicationFee)
                .add(insurance);

        SalaryLoan loan = SalaryLoanMapper.toEntity(request);
        loan.setInterestRate(interestRate);
        loan.setApplicationFee(applicationFee);
        loan.setLoanInsuranceFee(insurance);
        loan.setTotalPayableAmount(totalPayable);
        loan.setStatus(LoanStatus.PENDING);

        String appNumber = applicationNumberGenerator.generateApplicationNumber();
        loan.setApplicationNumber(appNumber);

        SalaryLoan savedLoan = salaryLoanRepository.save(loan);

        SalaryLoanResponse response = SalaryLoanMapper.toResponse(savedLoan);
        response.setRemarks("Application Fee: " + applicationFee + ", Insurance: " + insurance);
        return response;
    }

    private boolean isFirstLoanForCustomer(String customerId) {
        return salaryLoanRepository.findAll()
                .stream()
                .noneMatch(loan -> loan.getCustomerId().equals(customerId));
    }


    public SalaryLoanResponse getLoanById(String id) {
        SalaryLoan loan = salaryLoanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + id));
        return SalaryLoanMapper.toResponse(loan);
    }

    public List<SalaryLoanResponse> getAllLoans() {
        return salaryLoanRepository.findAll()
                .stream()
                .map(SalaryLoanMapper::toResponse)
                .collect(Collectors.toList());
    }

    public SalaryLoanResponse updateStatus(String id, LoanStatus status) {
        SalaryLoan loan = salaryLoanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + id));
        loan.setStatus(status);
        return SalaryLoanMapper.toResponse(salaryLoanRepository.save(loan));
    }
}
