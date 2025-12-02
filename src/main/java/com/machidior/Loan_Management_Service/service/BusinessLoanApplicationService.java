package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanApplicationResponse;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationApprovalRequest;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.BusinessLoanApplicationMapper;
import com.machidior.Loan_Management_Service.model.*;
import com.machidior.Loan_Management_Service.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BusinessLoanApplicationService {

    private final BusinessLoanApplicationRepository repository;
    private final LoanProductTermsRepository loanProductTermsRepository;
    private final LoanProductChargesRepository loanProductChargesRepository;
    private final LoanCalculationService calculator;
    private final BusinessLoanApplicationMapper mapper;
    private final LoanApplicationApprovalRepository loanApplicationApprovalRepository;
    private final BusinessLoanRepository businessLoanRepository;
    private final LoanApplicationRejectionRepository loanApplicationRejectionRepository;
    private final LoanApplicationReturnRepository loanApplicationReturnRepository;

    public BusinessLoanApplicationResponse applyBusinessLoan(BusinessLoanApplicationRequest request){



        LoanProductTerms terms = loanProductTermsRepository
                .findByProductType(LoanProductType.BUSINESS_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan terms not found for Business product"));

        LoanProductCharges charges = loanProductChargesRepository
                .findByProductType(LoanProductType.BUSINESS_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan charges not found for Business product"));

        BigDecimal requestedAmount = request.getAmountRequested();

        if (requestedAmount.compareTo(terms.getMinAmount()) < 0 ||
                requestedAmount.compareTo(terms.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Requested amount is outside the allowed range: " +
                    terms.getMinAmount() + " - " + terms.getMaxAmount());
        }

        if (request.getTermMonths()>terms.getMaximumTermMonths()) {
            throw new IllegalArgumentException("Term months must not exceed " + terms.getMaximumTermMonths() + " months");
        }

        BigDecimal interestRate = terms.getMonthlyInterestRate();


        boolean isFirstLoan = isFirstLoanForCustomer(request.getCustomerId());
        BigDecimal applicationFee = isFirstLoan
                ? charges.getFirstApplicationFee()
                : charges.getSubsequentApplicationFee();

        BigDecimal loanInsurance = requestedAmount
                .multiply(charges.getLoanInsurancePercent())
                .divide(BigDecimal.valueOf(100));

        BigDecimal totalInterest = requestedAmount
                .multiply(interestRate)
                .multiply(BigDecimal.valueOf(request.getTermMonths()))
                .divide(BigDecimal.valueOf(100));

        BusinessLoanApplication application = mapper.toEntity(request);

        application.setApplicationFee(applicationFee);
        application.setStatus(LoanApplicationStatus.PENDING);
        application.setAmountApproved(null);
        application.setInterestRate(interestRate);
        application.setLoanInsuranceFee(loanInsurance);
        application.setIsRead(false);
        application.setProductType(LoanProductType.BUSINESS_PRODUCT);

        BigDecimal loanFeeRate = ((request.getTermMonths() == 1))?
                calculator.calculateLoanFee(terms.getTotalInterestRatePerMonth(), interestRate)
                :calculator.calculateLoanFee(terms.getTotalInterestPer2Month(), interestRate.add(interestRate))
                .divide(BigDecimal.valueOf(2));
        application.setLoanFeeRate(loanFeeRate);

        return mapper.toResponse( repository.save(application));
    }

    private boolean isFirstLoanForCustomer(String customerId) {
        return repository.findAll()
                .stream()
                .noneMatch(loan -> loan.getCustomerId().equals(customerId));
    }

    public BusinessLoan approveBusinessLoanApplication(String applicationNumber, LoanApplicationApprovalRequest request){

        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        LoanApplicationApproval approval = new LoanApplicationApproval();
        approval.setApprovedAmount(request.getApprovedAmount());
        approval.setComments(request.getComments());
        approval.setApplicationNumber(application.getApplicationNumber());
        approval.setApprovedBy("manager");
        approval.setApprovedAt(LocalDateTime.now());
        loanApplicationApprovalRepository.save(approval);

        application.setAmountApproved(request.getApprovedAmount());
        application.setStatus(LoanApplicationStatus.APPROVED);
        BusinessLoanApplication approvedApplication = repository.save(application);

        BusinessLoan loan = getBusinessLoan(approvedApplication);
        return businessLoanRepository.save(loan);
    }

    private static BusinessLoan getBusinessLoan(BusinessLoanApplication approvedApplication) {
        BusinessLoan loan = new BusinessLoan();
        loan.setApplicationNumber(approvedApplication.getApplicationNumber());
        loan.setCustomerId(approvedApplication.getCustomerId());
        loan.setPrincipal(approvedApplication.getAmountApproved());
        loan.setInterestRate(approvedApplication.getInterestRate());
        loan.setLoanFeeRate(approvedApplication.getLoanFeeRate());
        loan.setTotalPayableAmount(null);
        loan.setTermMonths(approvedApplication.getTermMonths());
        loan.setProductType(approvedApplication.getProductType());
        loan.setLoanContract(null);
        loan.setStatus(LoanStatus.PENDING);
        loan.setInstallmentFrequency(approvedApplication.getInstallmentFrequency());
        return loan;
    }

    public BusinessLoanApplicationResponse rejectBusinessLoanApplication(String applicationNumber, String rejectionReason){

        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Business loan application with given application number is not found!"));

        LoanApplicationRejection rejection = new LoanApplicationRejection();
        rejection.setRejectedBy("manager");
        rejection.setRejectedAt(LocalDateTime.now());
        rejection.setApplicationNumber(application.getApplicationNumber());
        rejection.setRejectionReason(rejectionReason);
        loanApplicationRejectionRepository.save(rejection);

        application.setStatus(LoanApplicationStatus.REJECTED);
        return mapper.toResponse(repository.save(application));
    }

    public BusinessLoanApplicationResponse returnBusinessLoanApplication(String applicationNumber, String reasonOfReturn){

        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Business loan application with given application number is not found!"));

        LoanApplicationReturn loanApplicationReturn = new LoanApplicationReturn();
        loanApplicationReturn.setApplicationNumber(application.getApplicationNumber());
        loanApplicationReturn.setReturnedBy("manager");
        loanApplicationReturn.setReturnedAt(LocalDateTime.now());
        loanApplicationReturn.setReasonOfReturn(reasonOfReturn);
        loanApplicationReturnRepository.save(loanApplicationReturn);

        application.setStatus(LoanApplicationStatus.RETURNED);
        return mapper.toResponse(repository.save(application));
    }

}
