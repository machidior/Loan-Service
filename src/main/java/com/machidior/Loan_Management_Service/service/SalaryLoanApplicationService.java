package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.LoanApplicationApprovalRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanApplicationResponse;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.SalaryLoanApplicationMapper;
import com.machidior.Loan_Management_Service.model.*;
import com.machidior.Loan_Management_Service.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SalaryLoanApplicationService {

    private final SalaryLoanApplicationRepository repository;
    private final LoanProductTermsRepository loanProductTermsRepository;
    private final LoanProductChargesRepository loanProductChargesRepository;
    private final LoanCalculationService calculator;
    private final SalaryLoanApplicationMapper mapper;
    private final LoanApplicationApprovalRepository loanApplicationApprovalRepository;
    private final SalaryLoanRepository salaryLoanRepository;
    private final LoanApplicationRejectionRepository loanApplicationRejectionRepository;
    private final LoanApplicationReturnRepository loanApplicationReturnRepository;

    public SalaryLoanApplicationResponse applySalaryLoan(SalaryLoanApplicationRequest request){

        LoanProductTerms terms = loanProductTermsRepository
                .findByProductType(LoanProductType.SALARY_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan terms not found for SALARY_PRODUCT"));

        LoanProductCharges charges = loanProductChargesRepository
                .findByProductType(LoanProductType.SALARY_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan charges not found for SALARY_PRODUCT"));

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

        SalaryLoanApplication application = mapper.toEntity(request);

        application.setApplicationFee(applicationFee);
        application.setStatus(LoanApplicationStatus.PENDING);
        application.setAmountApproved(null);
        application.setInterestRate(interestRate);
        application.setLoanInsuranceFee(loanInsurance);
        application.setIsRead(false);
        application.setProductType(LoanProductType.SALARY_PRODUCT);


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

    public SalaryLoan approveSalaryLoanApplication(String applicationNumber, LoanApplicationApprovalRequest request){
        SalaryLoanApplication application = repository.findById(applicationNumber)
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
        SalaryLoanApplication approvedApplication = repository.save(application);

        SalaryLoan loan = getSalaryLoan(approvedApplication);
        return salaryLoanRepository.save(loan);
    }

    private static SalaryLoan getSalaryLoan(SalaryLoanApplication approvedApplication) {
        SalaryLoan loan = new SalaryLoan();
        loan.setApplicationNumber(approvedApplication.getApplicationNumber());
        loan.setCustomerId(approvedApplication.getCustomerId());
        loan.setPrincipal(approvedApplication.getAmountApproved());
        loan.setInterestRate(approvedApplication.getInterestRate());
        loan.setLoanFeeRate(approvedApplication.getLoanFeeRate());
        loan.setTotalPayableAmount(null);
        loan.setTermMonths(approvedApplication.getTermMonths());
        loan.setLoanContract(null);
        loan.setStatus(LoanStatus.PENDING);
        loan.setInstallmentFrequency(approvedApplication.getInstallmentFrequency());
        return loan;
    }

    public SalaryLoanApplicationResponse rejectSalaryLoanApplication(String applicationNumber, String rejectionReason){

        SalaryLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Salary loan application with given application number is not found!"));

        LoanApplicationRejection rejection = new LoanApplicationRejection();
        rejection.setRejectedBy("manager");
        rejection.setRejectedAt(LocalDateTime.now());
        rejection.setApplicationNumber(application.getApplicationNumber());
        rejection.setRejectionReason(rejectionReason);
        loanApplicationRejectionRepository.save(rejection);

        application.setStatus(LoanApplicationStatus.REJECTED);
        return mapper.toResponse(repository.save(application));
    }

    public SalaryLoanApplicationResponse returnSalaryLoanApplication(String applicationNumber, String reasonOfReturn){

        SalaryLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Salary loan application with given application number is not found!"));

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
