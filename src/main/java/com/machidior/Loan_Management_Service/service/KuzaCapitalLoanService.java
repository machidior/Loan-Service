//package com.machidior.Loan_Management_Service.service;
//
//import com.machidior.Loan_Management_Service.dtos.BusinessLoanRequest;
//import com.machidior.Loan_Management_Service.dtos.BusinessLoanResponse;
//import com.machidior.Loan_Management_Service.dtos.KuzaCapitalLoanRequest;
//import com.machidior.Loan_Management_Service.dtos.KuzaCapitalLoanResponse;
//import com.machidior.Loan_Management_Service.enums.LoanProductType;
//import com.machidior.Loan_Management_Service.enums.LoanStatus;
//import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
//import com.machidior.Loan_Management_Service.generator.ApplicationNumberGenerator;
//import com.machidior.Loan_Management_Service.mapper.BusinessLoanMapper;
//import com.machidior.Loan_Management_Service.mapper.KuzaCapitalLoanMapper;
//import com.machidior.Loan_Management_Service.model.BusinessLoan;
//import com.machidior.Loan_Management_Service.model.KuzaCapitalLoan;
//import com.machidior.Loan_Management_Service.model.LoanProductCharges;
//import com.machidior.Loan_Management_Service.model.LoanProductTerms;
//import com.machidior.Loan_Management_Service.repo.BusinessLoanRepository;
//import com.machidior.Loan_Management_Service.repo.KuzaCapitalLoanRepository;
//import com.machidior.Loan_Management_Service.repo.LoanProductChargesRepository;
//import com.machidior.Loan_Management_Service.repo.LoanProductTermsRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class KuzaCapitalLoanService  {
//
//    private final KuzaCapitalLoanRepository kuzaCapitalLoanRepository;
//    private final KuzaCapitalLoanMapper kuzaCapitalLoanMapper;
//    private final LoanProductTermsRepository loanProductTermsRepository;
//    private final LoanProductChargesRepository loanProductChargesRepository;
//    @Autowired
//    private ApplicationNumberGenerator applicationNumberGenerator;
//
//
//    public KuzaCapitalLoanResponse createKuzaCapitalLoan(KuzaCapitalLoanRequest request) {
//
//        LoanProductTerms terms = loanProductTermsRepository
//                .findByProductType(LoanProductType.KUZA_CAPITAL)
//                .orElseThrow(() -> new IllegalArgumentException("Loan terms not found for kuza capital"));
//
//        LoanProductCharges charges = loanProductChargesRepository
//                .findByProductType(LoanProductType.KUZA_CAPITAL)
//                .orElseThrow(() -> new IllegalArgumentException("Loan charges not found for kuza capital"));
//
//        BigDecimal requestedAmount = request.getAmountRequested();
//
//        if (requestedAmount.compareTo(terms.getMinAmount()) < 0 ||
//                requestedAmount.compareTo(terms.getMaxAmount()) > 0) {
//            throw new IllegalArgumentException("Requested amount is outside the allowed range: " +
//                    terms.getMinAmount() + " - " + terms.getMaxAmount());
//        }
//
//        if (!terms.getRepaymentDurationMonths().equals(request.getTermMonths())) {
//            throw new IllegalArgumentException("Repayment duration must be " + terms.getRepaymentDurationMonths() + " months");
//        }
//
//        BigDecimal interestRate = terms.getInterestRate();
//
//
//        boolean isFirstLoan = isFirstLoanForCustomer(request.getCustomerId());
//        BigDecimal applicationFee = isFirstLoan
//                ? charges.getFirstApplicationFee()
//                : charges.getSubsequentApplicationFee();
//
//        BigDecimal loanInsurance = requestedAmount
//                .multiply(charges.getLoanInsurancePercent())
//                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
//
//        BigDecimal totalInterest = requestedAmount
//                .multiply(interestRate)
//                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
//
//        BigDecimal totalPayable = requestedAmount
//                .add(totalInterest)
//                .add(applicationFee)
//                .add(loanInsurance);
//
//        KuzaCapitalLoan loan = kuzaCapitalLoanMapper.toEntity(request);
//
//        loan.setInterestRate(interestRate);
//        loan.setApplicationFee(applicationFee);
//        loan.setLoanInsuranceFee(loanInsurance);
//        loan.setTotalPayableAmount(totalPayable);
//        loan.setStatus(LoanStatus.PENDING);
//
//        String appNumber = applicationNumberGenerator.generateApplicationNumber();
//        loan.setApplicationNumber(appNumber);
//
//
//        KuzaCapitalLoan saved = kuzaCapitalLoanRepository.save(loan);
//        return kuzaCapitalLoanMapper.toResponse(saved);
//    }
//
//    private boolean isFirstLoanForCustomer(String customerId) {
//        return kuzaCapitalLoanRepository.findAll()
//                .stream()
//                .noneMatch(loan -> loan.getCustomerId().equals(customerId));
//    }
//
//    public KuzaCapitalLoanResponse getKuzaCapitalLoanById(String id) {
//        KuzaCapitalLoan loan = kuzaCapitalLoanRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Kuza capital not found with id: " + id));
//        return kuzaCapitalLoanMapper.toResponse(loan);
//    }
//
//    public KuzaCapitalLoanResponse getKuzaCapitalLoanByApplicationNumber(String applicationNumber) {
//        KuzaCapitalLoan loan = kuzaCapitalLoanRepository.findByApplicationNumber(applicationNumber)
//                .orElseThrow(() -> new ResourceNotFoundException("Kuza capital Loan not found with application number: " + applicationNumber));
//        return kuzaCapitalLoanMapper.toResponse(loan);
//    }
//
//    public List<KuzaCapitalLoanResponse> getAllKuzaCapitalLoans() {
//        return kuzaCapitalLoanRepository.findAll()
//                .stream()
//                .map(kuzaCapitalLoanMapper::toResponse)
//                .toList();
//    }
//
//    public KuzaCapitalLoanResponse updateKuzaCapitalLoan(String id, KuzaCapitalLoanRequest request) {
//        KuzaCapitalLoan existing = kuzaCapitalLoanRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Business Loan not found with id: " + id));
//
//        existing.setAmountApproved(request.getAmountApproved());
//        existing.setInterestRate(request.getInterestRate());
//        existing.setTermMonths(request.getTermMonths());
//        existing.setRepaymentFrequency(request.getRepaymentFrequency());
//        existing.setPurpose(request.getPurpose());
//        existing.setLoanOfficerId(request.getLoanOfficerId());
//        existing.setRemarks(request.getRemarks());
//
//        existing.setBankStatement(request.getBankStatement());
//        existing.setInsuranceComprehensiveCover(request.getInsuranceComprehensiveCover());
//        existing.setBusinessLicense(request.getBusinessLicense());
//        existing.setTinCertificate(request.getTinCertificate());
//        existing.setTinNumber(request.getTinNumber());
//        existing.setBrelaCertificate(request.getBrelaCertificate());
//
//        KuzaCapitalLoan updated = kuzaCapitalLoanRepository.save(existing);
//        return kuzaCapitalLoanMapper.toResponse(updated);
//    }
//
//    public void deleteKuzaCapitalLoan(String id) {
//        if (!kuzaCapitalLoanRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Kuza capital Loan not found with id: " + id);
//        }
//        kuzaCapitalLoanRepository.deleteById(id);
//    }
//}
