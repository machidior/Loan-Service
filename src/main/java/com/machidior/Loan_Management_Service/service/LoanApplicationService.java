package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.BusinessDetailsMapper;
import com.machidior.Loan_Management_Service.mapper.CollateralMapper;
import com.machidior.Loan_Management_Service.mapper.GuarantorMapper;
import com.machidior.Loan_Management_Service.mapper.LoanApplicationMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final LoanApplicationMapper mapper;
    private final LoanCalculationService calculator;
    private final GuarantorMapper guarantorMapper;
    private final CollateralMapper collateralMapper;
    private final FileStorageService fileStorageService;
    private final BusinessDetailsMapper businessDetailsMapper;
    private final LoanRepository loanRepository;
    private final LoanApplicationApprovalRepository loanApplicationApprovalRepository;
    private final LoanApplicationRejectionRepository loanApplicationRejectionRepository;
    private final LoanApplicationReturnRepository loanApplicationReturnRepository;

    public LoanApplicationResponse createLoanApplicationDetails(ApplicationDetails request) {

//      ToDo: Fetching charges and terms by productId from the the loan configuration service should be implemented here.
        LoanProductCharges charges = new LoanProductCharges();
//        Testing data
        charges.setFirstApplicationFee(BigDecimal.valueOf(30000));
        charges.setSubsequentApplicationFee(BigDecimal.valueOf(10000));
        charges.setLoanInsurancePercent(BigDecimal.ONE);

        LoanProductTerms terms = new LoanProductTerms();
        terms.setMinAmount(BigDecimal.valueOf(300000));
        terms.setMaxAmount(BigDecimal.valueOf(1000000));
        terms.setMaximumTermMonths(4);
        terms.setMonthlyInterestRate(BigDecimal.valueOf(3.5));
        terms.setTotalInterestRatePerMonth(BigDecimal.valueOf(5));



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


        boolean isFirstLoan = isFirstLoanForCustomer(request.getCustomerId(), request.getProductId());
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

        LoanApplication application = mapper.toEntity(request);

        application.setApplicationFee(applicationFee);
        application.setStatus(LoanApplicationStatus.DRAFTED);
        application.setAmountApproved(null);
        application.setInterestRate(interestRate);
        application.setLoanInsuranceFee(loanInsurance);
        application.setIsRead(false);

        BigDecimal loanFeeRate = calculator.calculateLoanFee(terms.getTotalInterestRatePerMonth(), interestRate);
        application.setLoanFeeRate(loanFeeRate);

        return mapper.toResponse( repository.save(application));
    }

    private boolean isFirstLoanForCustomer(String customerID, Long productID) {
        List<LoanApplication> applications = repository.findByCustomerIdAndProductId(customerID, productID);
        return applications.isEmpty();
    }

    public LoanApplicationResponse saveGuarantor(
            String applicationNumber,
            MultipartFile passport,
            MultipartFile identificationCard,
            GuarantorRequest request) throws IOException {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
        application.setGuarantor(guarantorMapper.toEntity(request,application));
        LoanApplication updatedApplication = repository.save(application);

        String passportUrl = fileStorageService.saveGuarantorFiles(passport, updatedApplication.getApplicationNumber(), "PASSPORT");
        String identificationCardUrl = fileStorageService.saveGuarantorFiles(identificationCard, updatedApplication.getApplicationNumber(), "IDENTIFICATION-CARD");
        updatedApplication.getGuarantor().setPassportUrl(passportUrl);
        updatedApplication.getGuarantor().setIdentificationCardUrl(identificationCardUrl);

        return mapper.toResponse(repository.save(updatedApplication));
    }

    public LoanApplicationResponse saveCollaterals(String applicationNumber, List<CollateralRequest> collateralRequests, List<MultipartFile> photos, List<MultipartFile> ownershipProofs) throws IOException {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        if (collateralRequests.size() != photos.size()){
            throw new IllegalArgumentException("Collateral count and photo count must match!");
        }

        List<Collateral> collaterals = new ArrayList<>();
        for (int i=0; i<collateralRequests.size(); i++){
            MultipartFile photo = photos.get(i);
            MultipartFile ownershipProof = ownershipProofs.get(i);
            String photoUrl = fileStorageService.saveCollateralFiles(photo, application.getApplicationNumber(),"COLLATERAL-PHOTO");
            String ownershipProofUrl = fileStorageService.saveCollateralFiles(ownershipProof, application.getApplicationNumber(), "OWNERSHIP-PROOF");

            Collateral collateral = collateralMapper.toEntity(collateralRequests.get(i),application);
            collateral.setPhotoUrl(photoUrl);
            collateral.setOwnershipProofUrl(ownershipProofUrl);

            collaterals.add(collateral);
        }
        application.setCollaterals(collaterals);
        return mapper.toResponse(repository.save(application));
    }

    public LoanApplicationResponse saveBusinessDetails(
            String applicationNumber,
            MultipartFile bankStatement,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile tinCertificate,
            MultipartFile brelaCertificate,
            MultipartFile businessLicense,
            BusinessDetailsRequest request) throws IOException {

        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        String bankStatementUrl = fileStorageService.saveBusinessFiles(bankStatement, application.getApplicationNumber(), "BANK-STATEMENT");
        String insuranceComprehensiveCoverUrl = fileStorageService.saveBusinessFiles(insuranceComprehensiveCover, application.getApplicationNumber(), "INSURANCE-COVER");
        String tinCertificateUrl = fileStorageService.saveBusinessFiles(tinCertificate, application.getApplicationNumber(), "TIN-CERTIFICATE");
        String brelaCertificateUrl = fileStorageService.saveBusinessFiles(brelaCertificate, application.getApplicationNumber(), "BRELA-LICENSE");
        String businessLicenseUrl = fileStorageService.saveBusinessFiles(businessLicense, application.getApplicationNumber(), "BUSINESS-LICENSE");

        BusinessDetails businessDetails = businessDetailsMapper.toEntity(request);
        businessDetails.setBankStatementUrl(bankStatementUrl);
        businessDetails.setInsuranceComprehensiveCoverUrl(insuranceComprehensiveCoverUrl);
        businessDetails.setTinCertificateUrl(tinCertificateUrl);
        businessDetails.setBrelaCertificateUrl(brelaCertificateUrl);
        businessDetails.setBusinessLicenseUrl(businessLicenseUrl);

        application.setBusinessDetails(businessDetails);

        return mapper.toResponse(repository.save(application));
    }

    public LoanApplicationResponse saveJobDetails(
            String applicationNumber,
            MultipartFile bankStatement,
            MultipartFile salarySlip,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile jobContract,
            JobDetails jobDetails
    ) throws IOException {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        application.setJobDetails(jobDetails);
        LoanApplication updatedApplication = repository.save(application);

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

//   ToDo: check the product requirements before creating application...
    public LoanApplicationResponse applyLoan(String applicationNumber){

        LoanApplication application = repository.findById(applicationNumber)
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

    public Loan approveLoanApplication(String applicationNumber, LoanApplicationApprovalRequest request){
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        LoanApplicationApproval approval = new LoanApplicationApproval();
        approval.setApprovedAmount(request.getApprovedAmount());
        approval.setComments(request.getComments());
        approval.setApplicationNumber(application.getApplicationNumber());
//        BUG: getUserId from the token
        approval.setApprovedBy("manager");
        approval.setApprovedAt(LocalDateTime.now());
        loanApplicationApprovalRepository.save(approval);

        application.setAmountApproved(request.getApprovedAmount());
        application.setStatus(LoanApplicationStatus.APPROVED);
        LoanApplication approvedApplication = repository.save(application);

        Loan loan = new Loan();
        loan.setApplicationNumber(approvedApplication.getApplicationNumber());
        loan.setCustomerId(approvedApplication.getCustomerId());
        loan.setPrincipal(approvedApplication.getAmountApproved());
        loan.setInterestRate(approvedApplication.getInterestRate());
        loan.setLoanFeeRate(approvedApplication.getLoanFeeRate());
//        ToDo: implement total payable amount of the application from the product configurations [ interest calculation method used ]
        loan.setTotalPayableAmount(null); // should not be null
        loan.setProductId(approvedApplication.getProductId());
        loan.setProductName(approvedApplication.getProductName());
        loan.setTermMonths(approvedApplication.getTermMonths());
        loan.setLoanContractUrl(null);
        loan.setAppliedOn(approvedApplication.getCreatedAt());
        loan.setApprovedOn(LocalDateTime.now());
        loan.setStatus(LoanStatus.PENDING);
        loan.setInstallmentFrequency(approvedApplication.getInstallmentFrequency());

        return loanRepository.save(loan);
    }

    public LoanApplicationResponse rejectLoanApplication(String applicationNumber, String rejectionReason){

        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application with given application number is not found!"));

        LoanApplicationRejection rejection = new LoanApplicationRejection();
//        BUG: getUserId from the token
        rejection.setRejectedBy("manager");
        rejection.setRejectedAt(LocalDateTime.now());
        rejection.setApplicationNumber(application.getApplicationNumber());
        rejection.setRejectionReason(rejectionReason);
        loanApplicationRejectionRepository.save(rejection);

        application.setStatus(LoanApplicationStatus.REJECTED);
        return mapper.toResponse(repository.save(application));
    }

    public LoanApplicationResponse returnLoanApplication(String applicationNumber, String reasonOfReturn){

        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application with given application number is not found!"));

        LoanApplicationReturn loanApplicationReturn = new LoanApplicationReturn();
        loanApplicationReturn.setApplicationNumber(application.getApplicationNumber());
//        BUG: getUserId from the token
        loanApplicationReturn.setReturnedBy("manager");
        loanApplicationReturn.setReturnedAt(LocalDateTime.now());
        loanApplicationReturn.setReasonOfReturn(reasonOfReturn);
        loanApplicationReturnRepository.save(loanApplicationReturn);

        application.setStatus(LoanApplicationStatus.RETURNED);
        return mapper.toResponse(repository.save(application));
    }

    public LoanApplicationResponse getLoanApplication(String applicationNumber) {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(()->new ResourceNotFoundException("There is no loan application with application number: " + applicationNumber));
        return mapper.toResponse(application);
    }

    public List<LoanApplicationResponse> getAllLoanApplications() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}
