package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanResponse;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.generator.ApplicationNumberGenerator;
import com.machidior.Loan_Management_Service.mapper.BusinessLoanMapper;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.LoanProductCharges;
import com.machidior.Loan_Management_Service.model.LoanProductTerms;
import com.machidior.Loan_Management_Service.repo.BusinessLoanRepository;
import com.machidior.Loan_Management_Service.repo.LoanProductChargesRepository;
import com.machidior.Loan_Management_Service.repo.LoanProductTermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessLoanService  {

    private final BusinessLoanRepository businessLoanRepository;
    private final BusinessLoanMapper businessLoanMapper;
    private final LoanProductTermsRepository loanProductTermsRepository;
    private final LoanProductChargesRepository loanProductChargesRepository;
    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;


    public BusinessLoanResponse createBusinessLoan(BusinessLoanRequest request) {

        LoanProductTerms terms = loanProductTermsRepository
                .findByProductType(LoanProductType.BUSINESS_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan terms not found for BUSINESS_PRODUCT"));

        LoanProductCharges charges = loanProductChargesRepository
                .findByProductType(LoanProductType.BUSINESS_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan charges not found for BUSINESS_PRODUCT"));

        BigDecimal requestedAmount = request.getAmountRequested();

        if (requestedAmount.compareTo(terms.getMinAmount()) < 0 ||
                requestedAmount.compareTo(terms.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Requested amount is outside the allowed range: " +
                    terms.getMinAmount() + " - " + terms.getMaxAmount());
        }

        if (!terms.getRepaymentDurationMonths().equals(request.getTermMonths())) {
            throw new IllegalArgumentException("Repayment duration must be " + terms.getRepaymentDurationMonths() + " months");
        }

        BigDecimal interestRate = terms.getInterestRate();


         boolean isFirstLoan = isFirstLoanForCustomer(request.getCustomerId());
         BigDecimal applicationFee = isFirstLoan
                 ? charges.getFirstApplicationFee()
                 : charges.getSubsequentApplicationFee();

        BigDecimal loanInsurance = requestedAmount
                .multiply(charges.getLoanInsurancePercent())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalInterest = requestedAmount
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);

        BigDecimal totalPayable = requestedAmount
                .add(totalInterest)
                .add(applicationFee)
                .add(loanInsurance);

        BusinessLoan loan = businessLoanMapper.toEntity(request);

        loan.setInterestRate(interestRate);
        loan.setApplicationFee(applicationFee);
        loan.setLoanInsuranceFee(loanInsurance);
        loan.setTotalPayableAmount(totalPayable);
        loan.setStatus(LoanStatus.PENDING);

        String appNumber = applicationNumberGenerator.generateApplicationNumber();
        loan.setApplicationNumber(appNumber);


        BusinessLoan saved = businessLoanRepository.save(loan);
        return businessLoanMapper.toResponse(saved);
    }

    private boolean isFirstLoanForCustomer(String customerId) {
        return businessLoanRepository.findAll()
                .stream()
                .noneMatch(loan -> loan.getCustomerId().equals(customerId));
    }

    public BusinessLoanResponse getBusinessLoanById(String id) {
        BusinessLoan loan = businessLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business Loan not found with id: " + id));
        return businessLoanMapper.toResponse(loan);
    }

    public BusinessLoanResponse getBusinessLoanByApplicationNumber(String applicationNumber) {
        BusinessLoan loan = businessLoanRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Business Loan not found with application number: " + applicationNumber));
        return businessLoanMapper.toResponse(loan);
    }

    public List<BusinessLoanResponse> getAllBusinessLoans() {
        return businessLoanRepository.findAll()
                .stream()
                .map(businessLoanMapper::toResponse)
                .toList();
    }

    public BusinessLoanResponse updateBusinessLoan(String id, BusinessLoanRequest request) {
        BusinessLoan existing = businessLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business Loan not found with id: " + id));

        existing.setAmountApproved(request.getAmountApproved());
        existing.setInterestRate(request.getInterestRate());
        existing.setTermMonths(request.getTermMonths());
        existing.setRepaymentFrequency(request.getRepaymentFrequency());
        existing.setPurpose(request.getPurpose());
        existing.setLoanOfficerId(request.getLoanOfficerId());
        existing.setRemarks(request.getRemarks());

        existing.setBankStatement(request.getBankStatement());
        existing.setInsuranceComprehensiveCover(request.getInsuranceComprehensiveCover());
        existing.setBusinessLicense(request.getBusinessLicense());
        existing.setTinCertificate(request.getTinCertificate());
        existing.setTinNumber(request.getTinNumber());
        existing.setBrelaCertificate(request.getBrelaCertificate());

        BusinessLoan updated = businessLoanRepository.save(existing);
        return businessLoanMapper.toResponse(updated);
    }

    public void deleteBusinessLoan(String id) {
        if (!businessLoanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Business Loan not found with id: " + id);
        }
        businessLoanRepository.deleteById(id);
    }
}
