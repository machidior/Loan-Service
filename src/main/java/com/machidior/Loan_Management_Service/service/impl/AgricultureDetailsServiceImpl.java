package com.machidior.Loan_Management_Service.service.impl;

import com.machidior.Loan_Management_Service.completion.RequirementCompletionService;
import com.machidior.Loan_Management_Service.dtos.request.AgricultureApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.data.AgricultureRequirementData;
import com.machidior.Loan_Management_Service.evaluator.requirement.AgricultureRequirementEvaluator;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.mapper.AgricultureRequirementMapper;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.model.requirement.AgricultureRequirementDetails;
import com.machidior.Loan_Management_Service.repo.AgricultureRepository;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRepository;
import com.machidior.Loan_Management_Service.service.AgricultureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgricultureDetailsServiceImpl implements AgricultureService {

    private final AgricultureRequirementMapper agricultureRequirementMapper;
    private final AgricultureRequirementEvaluator agricultureRequirementEvaluator;
    private final LoanApplicationRepository loanApplicationRepository;
    private final AgricultureRepository repository;
    private final ProductConfigurationsClient productConfigurationsClient;
    private final RequirementCompletionService requirementCompletionService;



    @Transactional
    @Override
    public RequirementSubmissionResponse saveAgriculturalDetails(
            String applicationNumber,
            AgricultureApplicationRequest request
    ) {
        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
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

        AgricultureRequirementDetails saved = repository.save(agricultureRequirementMapper.toEntity(request));
        requirementCompletionService.markCompleted(
                saved.getLoanApplication().getApplicationNumber(),
                RequirementType.AGRICULTURE);

        return new RequirementSubmissionResponse(
                true,
                List.of()
        );
    }



//    public LoanApplicationResponse saveAgriculturalDetails(
//            String applicationNumber,
//            MultipartFile farmOwnershipDocument,
//            MultipartFile landInspectionReport,
//            MultipartFile cropPhotograph,
//            AgriculturalDetails agriculturalDetails
//    ) throws IOException {
//        LoanApplication application = repository.findById(applicationNumber)
//                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
//
//        application.setAgriculturalDetails(agriculturalDetails);
//        LoanApplication updatedApplication = repository.save(application);
//
//        String farmOwnershipDocumentUrl = fileStorageService.saveAgriculturalFiles(farmOwnershipDocument, updatedApplication.getApplicationNumber(),"FARM-OWNERSHIP-DOCUMENT");
//        String landInspectionReportUrl = fileStorageService.saveAgriculturalFiles(landInspectionReport, updatedApplication.getApplicationNumber(),"LAND-INSPECTION-REPORT");
//        String cropPhotographUrl = fileStorageService.saveAgriculturalFiles(cropPhotograph, updatedApplication.getApplicationNumber(),"CROP-PHOTOGRAPH");
//
//        updatedApplication.getAgriculturalDetails().setFarmOwnershipDocumentUrl(farmOwnershipDocumentUrl);
//        updatedApplication.getAgriculturalDetails().setLandInspectionReportUrl(landInspectionReportUrl);
//        updatedApplication.getAgriculturalDetails().setCropPhotographUrl(cropPhotographUrl);
//
//        return mapper.toResponse(repository.save(updatedApplication));
//    }
}
