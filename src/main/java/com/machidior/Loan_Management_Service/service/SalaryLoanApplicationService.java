package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.SalaryLoanApplicationMapper;
import com.machidior.Loan_Management_Service.mapper.SalaryLoanGuarantorMapper;
import com.machidior.Loan_Management_Service.model.*;
import com.machidior.Loan_Management_Service.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryLoanApplicationService {

    private final SalaryLoanApplicationRepository repository;
    private final LoanProductTermsRepository loanProductTermsRepository;
    private final LoanProductChargesRepository loanProductChargesRepository;
    private final LoanCalculationService calculator;
    private final SalaryLoanApplicationMapper mapper;
    private final SalaryLoanGuarantorMapper guarantorMapper;
    private final LoanApplicationApprovalRepository loanApplicationApprovalRepository;
    private final SalaryLoanRepository salaryLoanRepository;
    private final LoanApplicationRejectionRepository loanApplicationRejectionRepository;
    private final LoanApplicationReturnRepository loanApplicationReturnRepository;
    private final FileStorageService fileStorageService;

    public SalaryLoanApplicationResponse applySalaryLoan(ApplicationDetails details){

        LoanProductTerms terms = loanProductTermsRepository
                .findByProductType(LoanProductType.SALARY_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan terms not found for SALARY_PRODUCT"));

        LoanProductCharges charges = loanProductChargesRepository
                .findByProductType(LoanProductType.SALARY_PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Loan charges not found for SALARY_PRODUCT"));

        BigDecimal requestedAmount = details.getAmountRequested();

        if (requestedAmount.compareTo(terms.getMinAmount()) < 0 ||
                requestedAmount.compareTo(terms.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Requested amount is outside the allowed range: " +
                    terms.getMinAmount() + " - " + terms.getMaxAmount());
        }

        if (details.getTermMonths()>terms.getMaximumTermMonths()) {
            throw new IllegalArgumentException("Term months must not exceed " + terms.getMaximumTermMonths() + " months");
        }

        BigDecimal interestRate = terms.getMonthlyInterestRate();


        boolean isFirstLoan = isFirstLoanForCustomer(details.getCustomerId());
        BigDecimal applicationFee = isFirstLoan
                ? charges.getFirstApplicationFee()
                : charges.getSubsequentApplicationFee();

        BigDecimal loanInsurance = requestedAmount
                .multiply(charges.getLoanInsurancePercent())
                .divide(BigDecimal.valueOf(100));

        BigDecimal totalInterest = requestedAmount
                .multiply(interestRate)
                .multiply(BigDecimal.valueOf(details.getTermMonths()))
                .divide(BigDecimal.valueOf(100));

        SalaryLoanApplication application = mapper.toEntity(details);

        application.setApplicationFee(applicationFee);
        application.setStatus(LoanApplicationStatus.DRAFTED);
        application.setAmountApproved(null);
        application.setInterestRate(interestRate);
        application.setGuarantor(null);
        application.setCollaterals(null);
        application.setJobDetails(null);
        application.setLoanInsuranceFee(loanInsurance);
        application.setIsRead(false);
        application.setProductType(LoanProductType.SALARY_PRODUCT);


        BigDecimal loanFeeRate = ((details.getTermMonths() == 1))?
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

    public SalaryLoanApplicationResponse saveJobDetails(
            String applicationNumber,
            MultipartFile bankStatement,
            MultipartFile salarySlip,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile jobContract,
            JobDetails jobDetails
    ) throws IOException {
        SalaryLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        application.setJobDetails(jobDetails);
        SalaryLoanApplication updatedApplication = repository.save(application);

        String bankStatementUrl = fileStorageService.saveJobFiles(bankStatement,updatedApplication.getApplicationNumber(),"BANK-STATEMENT");
        String salarySlipUrl = fileStorageService.saveJobFiles(salarySlip, updatedApplication.getApplicationNumber(),"SALARY-SLIP");
        String insuranceComprehensiveCoverUrl = fileStorageService.saveJobFiles(insuranceComprehensiveCover, updatedApplication.getApplicationNumber(),"INSURANCE-COVER");
        String jobContractUrl = fileStorageService.saveJobFiles(jobContract, updatedApplication.getApplicationNumber(),"JOB-CONTRACT");

        updatedApplication.getJobDetails().setBankStatementUrl(bankStatementUrl);
        updatedApplication.getJobDetails().setSalarySlipUrl(salarySlipUrl);
        updatedApplication.getJobDetails().setInsuranceComprehensiveCoverUrl(insuranceComprehensiveCoverUrl);
        updatedApplication.getJobDetails().setJobContractUrl(jobContractUrl);

        return mapper.toResponse(repository.save(updatedApplication));
    }

    public SalaryLoanApplicationResponse saveLoanCollaterals(String applicationNumber, List<SalaryLoanCollateralRequest> collateralRequests, List<MultipartFile> photos) throws IOException {
        SalaryLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        if (collateralRequests.size() != photos.size()){
            throw new IllegalArgumentException("Collateral count and photo count must match!");
        }

        List<SalaryLoanCollateral> collaterals = new ArrayList<>();
        for (int i=0; i<collateralRequests.size(); i++){
            MultipartFile photo = photos.get(i);
            String photoUrl = fileStorageService.saveCollateralFiles(photo, application.getApplicationNumber(),"COLLATERAL-PHOTO");

            SalaryLoanCollateral collateral = getSalaryLoanCollateral(collateralRequests.get(i), application, photoUrl);

            collaterals.add(collateral);
        }
        application.setCollaterals(collaterals);
        return mapper.toResponse(repository.save(application));
    }

    public SalaryLoanApplicationResponse saveLoanGuarantor(
            String applicationNumber,
            MultipartFile passport,
            MultipartFile identificationCard,
            SalaryLoanGuarantorRequest request) throws IOException {
        SalaryLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
        application.setGuarantor(guarantorMapper.toEntity(request,application));
        SalaryLoanApplication updatedApplication = repository.save(application);

        String passportUrl = fileStorageService.saveGuarantorFiles(passport, updatedApplication.getApplicationNumber(), "PASSPORT");
        String identificationCardUrl = fileStorageService.saveGuarantorFiles(identificationCard, updatedApplication.getApplicationNumber(), "IDENTIFICATION-CARD");
        updatedApplication.getGuarantor().setPassportUrl(passportUrl);
        updatedApplication.getGuarantor().setIdentificationCardUrl(identificationCardUrl);

        return mapper.toResponse(repository.save(updatedApplication));
    }

    private static SalaryLoanCollateral getSalaryLoanCollateral(SalaryLoanCollateralRequest request, SalaryLoanApplication application, String photoUrl) {
        SalaryLoanCollateral collateral = new SalaryLoanCollateral();
        collateral.setCustomerId(application.getCustomerId());
        collateral.setType(request.getType());
        collateral.setName(request.getName());
        collateral.setDescription(request.getDescription());
        collateral.setLocation(request.getLocation());
        collateral.setPurchaseCondition(request.getPurchaseCondition());
        collateral.setCondition(request.getCondition());
        collateral.setPhotoUrl(photoUrl);
        collateral.setPurchaseDate(request.getPurchaseDate());
        collateral.setQuantity(request.getQuantity());
        collateral.setPurchasingValue(request.getPurchasingValue());
        collateral.setEstimatedValue(request.getEstimatedValue());
        collateral.setSalaryLoanApplication(application);
        return collateral;
    }

    public SalaryLoanApplicationResponse createApplication(String applicationNumber){

        SalaryLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application wi th application number " +applicationNumber + " is not found!"));

        if (application.getJobDetails() == null){
            throw new IllegalArgumentException("Please provide job details");
        }
        if (application.getGuarantor() == null){
            throw new IllegalArgumentException("Please provide guarantor details");
        }
        if (application.getCollaterals().isEmpty()){
            throw new IllegalArgumentException("Please provide loan collaterals");
        }

        application.setStatus(LoanApplicationStatus.PENDING);
        return mapper.toResponse(repository.save(application));
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
        loan.setProductType(approvedApplication.getProductType());
        loan.setTermMonths(approvedApplication.getTermMonths());
        loan.setLoanContractUrl(null);
        loan.setAppliedOn(approvedApplication.getCreatedAt());
        loan.setApprovedOn(LocalDateTime.now());
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

    public SalaryLoanApplicationResponse getLoanApplication(String applicationNumber){
        SalaryLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Loan application is not found!"));
        return mapper.toResponse(application);
    }

}
