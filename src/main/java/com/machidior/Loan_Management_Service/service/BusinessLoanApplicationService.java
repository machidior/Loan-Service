package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.BusinessLoanApplicationMapper;
import com.machidior.Loan_Management_Service.mapper.BusinessLoanCollateralMapper;
import com.machidior.Loan_Management_Service.mapper.BusinessLoanGuarantorMapper;
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
    private final BusinessLoanCollateralMapper collateralMapper;
    private final BusinessLoanGuarantorMapper guarantorMapper;
    private final FileStorageService fileStorageService;

    public BusinessLoanApplicationResponse applyBusinessLoan(ApplicationDetails request){

        LoanProductTerms terms = loanProductTermsRepository
                .findByProductType(LoanProductType.BUSINESS_PRODUCT)
                .orElseThrow(() -> new ResourceNotFoundException("Loan terms not found for Business product"));

        LoanProductCharges charges = loanProductChargesRepository
                .findByProductType(LoanProductType.BUSINESS_PRODUCT)
                .orElseThrow(() -> new ResourceNotFoundException("Loan charges not found for Business product"));

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
        application.setStatus(LoanApplicationStatus.DRAFTED);
        application.setAmountApproved(null);
        application.setInterestRate(interestRate);
        application.setLoanInsuranceFee(loanInsurance);
        application.setIsRead(false);
        application.setBusinessDetails(null);
        application.setCollaterals(null);
        application.setGuarantor(null);
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

    public BusinessLoanApplicationResponse saveBusinessDetails(
            String applicationNumber,
            MultipartFile bankStatement,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile tinCertificate,
            MultipartFile brelaCertificate,
            MultipartFile businessLicense,
            List<BusinessDetails> businessDetails) throws IOException {
        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
        businessDetails.forEach(detail -> detail.setBusinessLoanApplication(application));
        application.setBusinessDetails(businessDetails);
        BusinessLoanApplication savedApplication = repository.save(application);

        String bankStatementUrl = fileStorageService.saveBusinessFiles(bankStatement, savedApplication.getApplicationNumber(), "BANK-STATEMENT");
        String insuranceComprehensiveCoverUrl = fileStorageService.saveBusinessFiles(insuranceComprehensiveCover, savedApplication.getApplicationNumber(), "INSURANCE-COVER");
        String tinCertificateUrl = fileStorageService.saveBusinessFiles(tinCertificate, savedApplication.getApplicationNumber(), "TIN-CERTIFICATE");
        String brelaCertificateUrl = fileStorageService.saveBusinessFiles(brelaCertificate, savedApplication.getApplicationNumber(), "BRELA-LICENSE");
        String businessLicenseUrl = fileStorageService.saveBusinessFiles(businessLicense, savedApplication.getApplicationNumber(), "BUSINESS-LICENSE");

        savedApplication.setBankStatementUrl(bankStatementUrl);
        savedApplication.setInsuranceComprehensiveCoverUrl(insuranceComprehensiveCoverUrl);
        savedApplication.setTinCertificateUrl(tinCertificateUrl);
        savedApplication.setBrelaCertificateUrl(brelaCertificateUrl);
        savedApplication.setBusinessLicenseUrl(businessLicenseUrl);

        return mapper.toResponse(repository.save(savedApplication));
    }

    public BusinessLoanApplicationResponse saveBusinessCollaterals(String applicationNumber,  List<BusinessLoanCollateralRequest> collateralRequests, List<MultipartFile> photos) throws IOException {
        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        if (collateralRequests.size() != photos.size()){
            throw new IllegalArgumentException("Collateral count and photo count must match!");
        }

        List<BusinessLoanCollateral> collaterals = new ArrayList<>();
        for (int i=0; i<collateralRequests.size(); i++){
            MultipartFile photo = photos.get(i);
            String photoUrl = fileStorageService.saveCollateralFiles(photo, application.getApplicationNumber(),"COLLATERAL-PHOTO");

            BusinessLoanCollateral collateral = getBusinessLoanCollateral(collateralRequests.get(i), application, photoUrl);

            collaterals.add(collateral);
        }
        application.setCollaterals(collaterals);
        return mapper.toResponse(repository.save(application));
    }

    private static BusinessLoanCollateral getBusinessLoanCollateral(BusinessLoanCollateralRequest request, BusinessLoanApplication application, String photoUrl) {
        BusinessLoanCollateral collateral = new BusinessLoanCollateral();
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
        collateral.setBusinessLoanApplication(application);
        return collateral;
    }

    public BusinessLoanApplicationResponse saveBusinessGuarantor(
            String applicationNumber,
            MultipartFile passport,
            MultipartFile identificationCard,
            BusinessLoanGuarantorRequest request) throws IOException {
        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
        application.setGuarantor(guarantorMapper.toEntity(request,application));
        BusinessLoanApplication updatedApplication = repository.save(application);

        String passportUrl = fileStorageService.saveGuarantorFiles(passport, updatedApplication.getApplicationNumber(), "PASSPORT");
        String identificationCardUrl = fileStorageService.saveGuarantorFiles(identificationCard, updatedApplication.getApplicationNumber(), "IDENTIFICATION-CARD");
        updatedApplication.getGuarantor().setPassportUrl(passportUrl);
        updatedApplication.getGuarantor().setIdentificationCardUrl(identificationCardUrl);

        return mapper.toResponse(repository.save(updatedApplication));
    }

    public BusinessLoanApplicationResponse createApplication(String applicationNumber){

        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application wi th application number " +applicationNumber + " is not found!"));

        if (application.getBusinessDetails().isEmpty()){
            throw new IllegalArgumentException("Please provide business details");
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
        loan.setLoanContractUrl(null);
        loan.setAppliedOn(approvedApplication.getCreatedAt());
        loan.setApprovedOn(LocalDateTime.now());
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

    public BusinessLoanApplicationResponse getApplicationByApplicationNumber(String applicationNumber){
        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Business loan application with given application number is not found!"));
        return mapper.toResponse(application);
    }

    public void deleteLoanApplication(String applicationNumber) throws IOException {
        BusinessLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Business loan application with given application number is not found!"));
        fileStorageService.deleteBusinessFile(application.getBusinessLicenseUrl());
        fileStorageService.deleteBusinessFile(application.getInsuranceComprehensiveCoverUrl());
        fileStorageService.deleteBusinessFile(application.getBankStatementUrl());
        fileStorageService.deleteBusinessFile(application.getTinCertificateUrl());
        fileStorageService.deleteBusinessFile(application.getBrelaCertificateUrl());
        fileStorageService.deleteGuarantorFile(application.getGuarantor().getPassportUrl());
        fileStorageService.deleteGuarantorFile(application.getGuarantor().getIdentificationCardUrl());
        repository.deleteById(applicationNumber);
    }
}
