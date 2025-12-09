package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.KuzaLoanApplicationMapper;
import com.machidior.Loan_Management_Service.mapper.KuzaLoanGuarantorMapper;
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
public class KuzaLoanApplicationService {

    private final KuzaLoanApplicationRepository repository;
    private final LoanProductTermsRepository loanProductTermsRepository;
    private final LoanProductChargesRepository loanProductChargesRepository;
    private final LoanCalculationService calculator;
    private final KuzaLoanApplicationMapper mapper;
    private final KuzaLoanGuarantorMapper guarantorMapper;
    private final LoanApplicationApprovalRepository loanApplicationApprovalRepository;
    private final KuzaLoanRepository kuzaLoanRepository;
    private final LoanApplicationRejectionRepository loanApplicationRejectionRepository;
    private final LoanApplicationReturnRepository loanApplicationReturnRepository;
    private final FileStorageService fileStorageService;

    public KuzaLoanApplicationResponse applyKuzaLoan(ApplicationDetails applicationDetails){



        LoanProductTerms terms = loanProductTermsRepository
                .findByProductType(LoanProductType.KUZA_CAPITAL)
                .orElseThrow(() -> new ResourceNotFoundException("Loan terms not found for Business product"));

        LoanProductCharges charges = loanProductChargesRepository
                .findByProductType(LoanProductType.KUZA_CAPITAL)
                .orElseThrow(() -> new ResourceNotFoundException("Loan charges not found for Business product"));

        BigDecimal requestedAmount = applicationDetails.getAmountRequested();

        if (requestedAmount.compareTo(terms.getMinAmount()) < 0 ||
                requestedAmount.compareTo(terms.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Requested amount is outside the allowed range: " +
                    terms.getMinAmount() + " - " + terms.getMaxAmount());
        }

        if (applicationDetails.getTermMonths()>terms.getMaximumTermMonths()) {
            throw new IllegalArgumentException("Term months must not exceed " + terms.getMaximumTermMonths() + " months");
        }

        BigDecimal interestRate = terms.getMonthlyInterestRate();


        boolean isFirstLoan = isFirstLoanForCustomer(applicationDetails.getCustomerId());
        BigDecimal applicationFee = isFirstLoan
                ? charges.getFirstApplicationFee()
                : charges.getSubsequentApplicationFee();

        BigDecimal loanInsurance = requestedAmount
                .multiply(charges.getLoanInsurancePercent())
                .divide(BigDecimal.valueOf(100));

        BigDecimal totalInterest = requestedAmount
                .multiply(interestRate)
                .multiply(BigDecimal.valueOf(applicationDetails.getTermMonths()))
                .divide(BigDecimal.valueOf(100));

        KuzaLoanApplication application = mapper.toEntity(applicationDetails);

        application.setApplicationFee(applicationFee);
        application.setStatus(LoanApplicationStatus.DRAFTED);
        application.setAmountApproved(null);
        application.setInterestRate(interestRate);
        application.setLoanInsuranceFee(loanInsurance);
        application.setIsRead(false);
        application.setBusinessDetails(null);
        application.setCollaterals(null);
        application.setGuarantor(null);
        application.setProductType(LoanProductType.KUZA_CAPITAL);

        BigDecimal loanFeeRate = calculator.calculateLoanFee(terms.getTotalInterestRatePerMonth(), interestRate);
        application.setLoanFeeRate(loanFeeRate);

        return mapper.toResponse( repository.save(application));
    }

    private boolean isFirstLoanForCustomer(String customerId) {
        return repository.findAll()
                .stream()
                .noneMatch(loan -> loan.getCustomerId().equals(customerId));
    }

    public KuzaLoanApplicationResponse saveBusinessDetails(
            String applicationNumber,
            MultipartFile bankStatement,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile tinCertificate,
            MultipartFile brelaCertificate,
            MultipartFile businessLicense,
            List<KuzaBusinessDetails> businessDetails) throws IOException {
        KuzaLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
        businessDetails.forEach(detail -> detail.setKuzaLoanApplication(application));
        application.setBusinessDetails(businessDetails);
        KuzaLoanApplication savedApplication = repository.save(application);

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

    public KuzaLoanApplicationResponse saveLoanCollaterals(String applicationNumber,  List<KuzaLoanCollateralRequest> collateralRequests, List<MultipartFile> photos) throws IOException {
        KuzaLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        if (collateralRequests.size() != photos.size()){
            throw new IllegalArgumentException("Collateral count and photo count must match!");
        }

        List<KuzaLoanCollateral> collaterals = new ArrayList<>();
        for (int i=0; i<collateralRequests.size(); i++){
            MultipartFile photo = photos.get(i);
            String photoUrl = fileStorageService.saveCollateralFiles(photo, application.getApplicationNumber(),"COLLATERAL-PHOTO");

            KuzaLoanCollateral collateral = getKuzaLoanCollateral(collateralRequests.get(i), application, photoUrl);

            collaterals.add(collateral);
        }
        application.setCollaterals(collaterals);
        return mapper.toResponse(repository.save(application));
    }

    public KuzaLoanApplicationResponse saveLoanGuarantor(
            String applicationNumber,
            MultipartFile passport,
            MultipartFile identificationCard,
            KuzaLoanGuarantorRequest request) throws IOException {
        KuzaLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
        application.setGuarantor(guarantorMapper.toEntity(request,application));
        KuzaLoanApplication updatedApplication = repository.save(application);

        String passportUrl = fileStorageService.saveGuarantorFiles(passport, updatedApplication.getApplicationNumber(), "PASSPORT");
        String identificationCardUrl = fileStorageService.saveGuarantorFiles(identificationCard, updatedApplication.getApplicationNumber(), "IDENTIFICATION-CARD");
        updatedApplication.getGuarantor().setPassportUrl(passportUrl);
        updatedApplication.getGuarantor().setIdentificationCardUrl(identificationCardUrl);

        return mapper.toResponse(repository.save(updatedApplication));
    }

    private static KuzaLoanCollateral getKuzaLoanCollateral(KuzaLoanCollateralRequest request, KuzaLoanApplication application, String photoUrl) {
        KuzaLoanCollateral collateral = new KuzaLoanCollateral();
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
        collateral.setKuzaLoanApplication(application);
        return collateral;
    }

    public KuzaLoanApplicationResponse createApplication(String applicationNumber){

        KuzaLoanApplication application = repository.findById(applicationNumber)
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

    public KuzaLoan approveKuzaLoanApplication(String applicationNumber, LoanApplicationApprovalRequest request){

        KuzaLoanApplication application = repository.findById(applicationNumber)
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
        KuzaLoanApplication approvedApplication = repository.save(application);

        KuzaLoan loan = getKuzaLoan(approvedApplication);
        return kuzaLoanRepository.save(loan);
    }

    private static KuzaLoan getKuzaLoan(KuzaLoanApplication approvedApplication) {
        KuzaLoan loan = new KuzaLoan();
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

    public KuzaLoanApplicationResponse rejectKuzaLoanApplication(String applicationNumber, String rejectionReason){

        KuzaLoanApplication application = repository.findById(applicationNumber)
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

    public KuzaLoanApplicationResponse returnKuzaLoanApplication(String applicationNumber, String reasonOfReturn){

        KuzaLoanApplication application = repository.findById(applicationNumber)
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

    public KuzaLoanApplicationResponse getLoanApplication(String applicationNumber){
        KuzaLoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(()->new ResourceNotFoundException("Loan application not found"));
        return mapper.toResponse(application);
    }

}
