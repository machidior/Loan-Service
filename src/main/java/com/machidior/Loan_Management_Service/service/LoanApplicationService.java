package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.request.*;
import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.data.*;
import com.machidior.Loan_Management_Service.evaluator.requirement.AgricultureRequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.requirement.BusinessRequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.requirement.CollateralRequirementEvaluator;
import com.machidior.Loan_Management_Service.evaluator.requirement.GuarantorRequirementEvaluator;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.mapper.*;
import com.machidior.Loan_Management_Service.model.*;
import com.machidior.Loan_Management_Service.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final ProductConfigurationsClient productConfigurationsClient;

    private final AgricultureRequirementMapper agricultureRequirementMapper;
    private final AgricultureRequirementEvaluator agricultureRequirementEvaluator;
    private final BusinessRequirementEvaluator businessRequirementEvaluator;
    private final CollateralRequirementEvaluator collateralRequirementEvaluator;
    private final GuarantorRequirementEvaluator guarantorRequirementEvaluator;

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

    @Transactional
    public RequirementSubmissionResponse saveGuarantor(
            String applicationNumber,
            List<MultipartFile> passportPhotos,
            List<MultipartFile> identificationCards,
            List<MultipartFile> guarantorConsents,
            List<MultipartFile> incomeProofs,
            List<GuarantorRequest> guarantorRequests
    ) throws IOException {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        if (guarantorRequests.size() != passportPhotos.size()
                || guarantorRequests.size() != identificationCards.size()
                || guarantorRequests.size() != guarantorConsents.size()) {
            throw new IllegalArgumentException(
                    "Guarantor count, passport photo count, identification card count, and consent form count must match!"
            );
        }

        GuarantorRequirementData data = new GuarantorRequirementData();
        for (int i = 0; i < guarantorRequests.size(); i++) {
            GuarantorRequest req = guarantorRequests.get(i);
            GuarantorItemData item = new GuarantorItemData();
            item.setEmploymentProvided(req.getOccupation() != null && !req.getOccupation().isBlank());
            item.setIncome(req.getMonthlyIncome());
            item.setRelationProvided(req.getRelationship() != null && !req.getRelationship().isBlank());
            item.setIncomeProofProvided(
                    incomeProofs.get(i) != null && !incomeProofs.get(i).isEmpty()
            );
            item.setPassportPhotoProvided(
                    passportPhotos.get(i) != null && !passportPhotos.get(i).isEmpty()
            );
            item.setIdDocumentProvided(
                    identificationCards.get(i) != null && !identificationCards.get(i).isEmpty()
            );
            item.setGuarantorConsentProvided(
                    guarantorConsents.get(i) != null && !guarantorConsents.get(i).isEmpty()
            );

            data.getGuarantors().add(item);
        }

        Requirements requirements =
                productConfigurationsClient.getVersionRequirements(
                        application.getProductVersionId()
                );


        RequirementEvaluationResult result =
                guarantorRequirementEvaluator.evaluate(
                        data,
                        requirements.getGuarantorRequirement()
                );

        if (!result.isPassed()) {
            return new RequirementSubmissionResponse(
                    false,
                    result.getViolations()
            );
        }

        List<Guarantor> guarantors = new ArrayList<>();
        for (int i = 0; i < guarantorRequests.size(); i++) {
            GuarantorRequest req = guarantorRequests.get(i);
            Guarantor guarantor = guarantorMapper.toEntity(req, application);
            String passportUrl = fileStorageService.saveGuarantorFiles(
                    passportPhotos.get(i),
                    application.getApplicationNumber(),
                    "PASSPORT"
            );
            String identificationCardUrl = fileStorageService.saveGuarantorFiles(
                    identificationCards.get(i),
                    application.getApplicationNumber(),
                    "IDENTIFICATION-CARD"
            );
            String guarantorConsentUrl = fileStorageService.saveGuarantorFiles(
                    guarantorConsents.get(i),
                    application.getApplicationNumber(),
                    "GUARANTOR-CONSENT"
            );
            String incomeProofUrl = fileStorageService.saveGuarantorFiles(
                    incomeProofs.get(i),
                    application.getApplicationNumber(),
                    "INCOME-PROOF"
            );
            guarantor.setPassportUrl(passportUrl);
            guarantor.setIdentificationCardUrl(identificationCardUrl);
            guarantor.setGuarantorConsentUrl(guarantorConsentUrl);
            guarantor.setIncomeProofUrl(incomeProofUrl);
            guarantors.add(guarantor);
        }


        application.setGuarantors(guarantors);
        repository.save(application);

        return new RequirementSubmissionResponse(true, List.of());

    }

    @Transactional
    public RequirementSubmissionResponse saveCollaterals(
            String applicationNumber,
            List<CollateralRequest> collateralRequests,
            List<MultipartFile> photos,
            List<MultipartFile> ownershipProofs,
            List<MultipartFile> insuranceDocuments
    ) throws IOException {

        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan Application with the given application number is not found!"
                        )
                );

        if (collateralRequests.size() != photos.size()
                || collateralRequests.size() != ownershipProofs.size()) {
            throw new IllegalArgumentException(
                    "Collateral count, photo count, and ownership proof count must match!"
            );
        }

        CollateralRequirementData data = new CollateralRequirementData();
        BigDecimal totalCollateralValue = BigDecimal.ZERO;
        for (int i = 0; i < collateralRequests.size(); i++) {
            CollateralRequest req = collateralRequests.get(i);

            CollateralItemData item = new CollateralItemData();
            item.setType(CollateralType.valueOf(req.getType()));
            item.setDescriptionProvided(
                    req.getDescription() != null && !req.getDescription().isBlank()
            );
            item.setPhotoProvided(
                    photos.get(i) != null && !photos.get(i).isEmpty()
            );
            item.setOwnershipProofProvided(
                    ownershipProofs.get(i) != null && !ownershipProofs.get(i).isEmpty()
            );
            item.setInsuranceProvided(
                    insuranceDocuments.get(i) != null && !insuranceDocuments.get(i).isEmpty()
            );
            item.setValuationProvided(
                    req.getEstimatedValue() != null
            );
            totalCollateralValue = totalCollateralValue.add(
                    req.getEstimatedValue() != null ? req.getEstimatedValue() : BigDecimal.ZERO
            );
            data.getCollateralItems().add(item);
        }
        data.setTotalCollateralValue(totalCollateralValue);
        data.setRequestedAmount(application.getAmountRequested());

        Requirements requirements =
                productConfigurationsClient.getVersionRequirements(
                        application.getProductVersionId()
                );


        RequirementEvaluationResult result =
                collateralRequirementEvaluator.evaluate(
                        data,
                        requirements.getCollateralRequirement()
                );

        if (!result.isPassed()) {
            return new RequirementSubmissionResponse(
                    false,
                    result.getViolations()
            );
        }

        List<Collateral> collaterals = new ArrayList<>();

        for (int i = 0; i < collateralRequests.size(); i++) {

            CollateralRequest req = collateralRequests.get(i);

            String photoUrl = fileStorageService.saveCollateralFiles(
                    photos.get(i),
                    application.getApplicationNumber(),
                    "COLLATERAL-PHOTO"
            );

            String ownershipProofUrl = fileStorageService.saveCollateralFiles(
                    ownershipProofs.get(i),
                    application.getApplicationNumber(),
                    "OWNERSHIP-PROOF"
            );

            String insuranceDocumentUrl = fileStorageService.saveCollateralFiles(
                    ownershipProofs.get(i),
                    application.getApplicationNumber(),
                    "INSURANCE-DOCUMENT"
            );

            Collateral collateral =
                    collateralMapper.toEntity(req, application);

            collateral.setPhotoUrl(photoUrl);
            collateral.setOwnershipProofUrl(ownershipProofUrl);
            collateral.setInsuranceDocumentUrl(insuranceDocumentUrl);

            collaterals.add(collateral);
        }

        application.setCollaterals(collaterals);
        repository.save(application);

        return new RequirementSubmissionResponse(true, List.of());
    }

    @Transactional
    public RequirementSubmissionResponse saveBusinessDetails(
            String applicationNumber,
            MultipartFile cashFlowStatement,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile tinCertificate,
            MultipartFile brelaCertificate,
            MultipartFile businessLicense,
            BusinessDetailsRequest request) throws IOException {

        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        BusinessApplicationData evaluationData = new BusinessApplicationData();
        evaluationData.setBusinessLicenseProvided(businessLicense != null);
        evaluationData.setRegistered(brelaCertificate != null);
        evaluationData.setTinNumberProvided(request.getTinNumber() != null);
        evaluationData.setCashFlowStatementProvided(cashFlowStatement != null);
        evaluationData.setTinCertificateProvided(tinCertificate != null);
        evaluationData.setInsuranceCoverProvided(insuranceComprehensiveCover != null);
        evaluationData.setYearsInOperation(
                request.getBusinessList().stream()
                        .map(BusinessRequest::getYearsInOperation)
                        .max(Integer::compareTo)
                        .orElse(0)
        );
        evaluationData.setAverageMonthlyTurnover(request.getAverageMonthlyTurnover());

        Requirements requirements = productConfigurationsClient.getVersionRequirements(application.getProductVersionId());

        RequirementEvaluationResult result = businessRequirementEvaluator.evaluate(
                evaluationData,
                requirements.getBusinessRequirement()
        );
        if (!result.isPassed()) {
            return new RequirementSubmissionResponse(
                    false,
                    result.getViolations()
            );
        }

        assert cashFlowStatement != null;
        String cashFlowStatementUrl = fileStorageService.saveBusinessFiles(
                cashFlowStatement,
                application.getApplicationNumber(),
                "CASH-FLOW-STATEMENT"
        );
        assert insuranceComprehensiveCover != null;
        String insuranceComprehensiveCoverUrl = fileStorageService.saveBusinessFiles(
                insuranceComprehensiveCover,
                application.getApplicationNumber(),
                "INSURANCE-COVER"
        );
        assert tinCertificate != null;
        String tinCertificateUrl = fileStorageService.saveBusinessFiles(tinCertificate,
                application.getApplicationNumber(),
                "TIN-CERTIFICATE"
        );
        assert brelaCertificate != null;
        String brelaCertificateUrl = fileStorageService.saveBusinessFiles(
                brelaCertificate,
                application.getApplicationNumber(),
                "BRELA-LICENSE"
        );
        assert businessLicense != null;
        String businessLicenseUrl = fileStorageService.saveBusinessFiles(
                businessLicense,
                application.getApplicationNumber(),
                "BUSINESS-LICENSE"
        );

        BusinessDetails businessDetails = businessDetailsMapper.toEntity(request);
        businessDetails.setInsuranceComprehensiveCoverUrl(insuranceComprehensiveCoverUrl);
        businessDetails.setTinCertificateUrl(tinCertificateUrl);
        businessDetails.setBrelaCertificateUrl(brelaCertificateUrl);
        businessDetails.setBusinessLicenseUrl(businessLicenseUrl);
        businessDetails.setCashFlowStatementUrl(cashFlowStatementUrl);


        application.setBusinessDetails(businessDetails);
         repository.save(application);

        return new RequirementSubmissionResponse(
                true,
                List.of());
    }

    public LoanApplicationResponse saveJobDetails(
            String applicationNumber,
            MultipartFile bankStatement,
            MultipartFile salarySlip,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile jobContract,
            EmploymentDetails employmentDetails
    ) throws IOException {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        application.setEmploymentDetails(employmentDetails);
        LoanApplication updatedApplication = repository.save(application);

        String bankStatementUrl = fileStorageService.saveJobFiles(bankStatement,updatedApplication.getApplicationNumber(),"BANK-STATEMENT");
        String salarySlipUrl = fileStorageService.saveJobFiles(salarySlip, updatedApplication.getApplicationNumber(),"SALARY-SLIP");
        String insuranceComprehensiveCoverUrl = fileStorageService.saveJobFiles(insuranceComprehensiveCover, updatedApplication.getApplicationNumber(),"INSURANCE-COVER");
        String jobContractUrl = fileStorageService.saveJobFiles(jobContract, updatedApplication.getApplicationNumber(),"JOB-CONTRACT");

        updatedApplication.getEmploymentDetails().setBankStatementUrl(bankStatementUrl);
        updatedApplication.getEmploymentDetails().setSalarySlipUrl(salarySlipUrl);
        updatedApplication.getEmploymentDetails().setInsuranceComprehensiveCoverUrl(insuranceComprehensiveCoverUrl);
        updatedApplication.getEmploymentDetails().setJobContractUrl(jobContractUrl);

        return mapper.toResponse(repository.save(updatedApplication));
    }

    @Transactional
    public RequirementSubmissionResponse saveAgriculturalDetails(
            String applicationNumber,
            AgricultureApplicationRequest request
    ) {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
        AgricultureRequirementData data = agricultureRequirementMapper.toEvaluationData(request);

        Requirements requirements = productConfigurationsClient.getVersionRequirements(application.getProductVersionId());

        RequirementEvaluationResult result = agricultureRequirementEvaluator.evaluate(data,requirements.getAgricultureRequirement());

        if (!result.isPassed()) {
            return new RequirementSubmissionResponse(
                    false,
                    result.getViolations()
            );
        }
        application.setAgricultureRequirementDetails(agricultureRequirementMapper.toEntity(request));
        repository.save(application);
        return new RequirementSubmissionResponse(
                true,
                List.of()
        );
    }

//   ToDo: check the product requirements before creating application...
    public LoanApplicationResponse applyLoan(String applicationNumber){

        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application wi th application number " +applicationNumber + " is not found!"));

        if (application.getEmploymentDetails() == null){
            throw new IllegalArgumentException("Please provide job details");
        }
        if (application.getGuarantors() == null || application.getGuarantors().isEmpty()){
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
