package com.machidior.Loan_Management_Service.service.impl;

import com.machidior.Loan_Management_Service.completion.RequirementCompletionService;
import com.machidior.Loan_Management_Service.dtos.request.BusinessDetailsRequest;
import com.machidior.Loan_Management_Service.dtos.request.BusinessRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.data.BusinessApplicationData;
import com.machidior.Loan_Management_Service.evaluator.requirement.BusinessRequirementEvaluator;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.mapper.BusinessDetailsMapper;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.model.requirement.BusinessDetails;
import com.machidior.Loan_Management_Service.repo.BusinessDetailsRepository;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRepository;
import com.machidior.Loan_Management_Service.service.BusinessDetailsService;
import com.machidior.Loan_Management_Service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessDetailsServiceImpl implements BusinessDetailsService {

    private final BusinessDetailsRepository repository;
    private final BusinessRequirementEvaluator businessRequirementEvaluator;
    private final BusinessDetailsMapper businessDetailsMapper;
    private final ProductConfigurationsClient productConfigurationsClient;
    private final LoanApplicationRepository loanApplicationRepository;
    private final FileStorageService fileStorageService;
    private final RequirementCompletionService requirementCompletionService;




    @Transactional
    @Override
    public RequirementSubmissionResponse saveBusinessDetails(
            String applicationNumber,
            MultipartFile cashFlowStatement,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile tinCertificate,
            MultipartFile brelaCertificate,
            MultipartFile businessLicense,
            BusinessDetailsRequest request) throws IOException {

        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
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

        BusinessDetails businessDetails = businessDetailsMapper.toEntity(request, application);
        businessDetails.setInsuranceComprehensiveCoverUrl(insuranceComprehensiveCoverUrl);
        businessDetails.setTinCertificateUrl(tinCertificateUrl);
        businessDetails.setBrelaCertificateUrl(brelaCertificateUrl);
        businessDetails.setBusinessLicenseUrl(businessLicenseUrl);
        businessDetails.setCashFlowStatementUrl(cashFlowStatementUrl);
        BusinessDetails saved = repository.save(businessDetails);
        requirementCompletionService.markCompleted(
                saved.getLoanApplication().getApplicationNumber(),
                RequirementType.BUSINESS_DETAILS
        );

        return new RequirementSubmissionResponse(
                true,
                List.of());
    }
}
