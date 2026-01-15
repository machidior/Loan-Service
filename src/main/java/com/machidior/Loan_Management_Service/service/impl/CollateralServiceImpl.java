package com.machidior.Loan_Management_Service.service.impl;

import com.machidior.Loan_Management_Service.completion.RequirementCompletionService;
import com.machidior.Loan_Management_Service.dtos.request.CollateralRequest;
import com.machidior.Loan_Management_Service.dtos.response.CollateralResponse;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.enums.CollateralType;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.data.CollateralItemData;
import com.machidior.Loan_Management_Service.evaluator.data.CollateralRequirementData;
import com.machidior.Loan_Management_Service.evaluator.requirement.CollateralRequirementEvaluator;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.mapper.CollateralMapper;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.model.requirement.Collateral;
import com.machidior.Loan_Management_Service.repo.CollateralRepository;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRepository;
import com.machidior.Loan_Management_Service.service.CollateralService;
import com.machidior.Loan_Management_Service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollateralServiceImpl implements CollateralService {

    private final CollateralRepository repository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final CollateralMapper mapper;
    private final CollateralRequirementEvaluator collateralRequirementEvaluator;
    private final FileStorageService fileStorageService;
    private final ProductConfigurationsClient productConfigurationsClient;
    private final RequirementCompletionService requirementCompletionService;



    @Transactional
    @Override
    public RequirementSubmissionResponse saveCollaterals(
            String applicationNumber,
            List<CollateralRequest> collateralRequests,
            List<MultipartFile> photos,
            List<MultipartFile> ownershipProofs,
            List<MultipartFile> insuranceDocuments
    ) throws IOException {

        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
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
                    mapper.toEntity(req, application);

            collateral.setPhotoUrl(photoUrl);
            collateral.setOwnershipProofUrl(ownershipProofUrl);
            collateral.setInsuranceDocumentUrl(insuranceDocumentUrl);

            collaterals.add(collateral);
        }

        repository.saveAll(collaterals);
        requirementCompletionService.markCompleted(
                applicationNumber,
                RequirementType.COLLATERAL
        );

        return new RequirementSubmissionResponse(true, List.of());
    }


    @Override
    public List<CollateralResponse> getLoanCollaterals(String applicationNumber){
        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Loan Application not found!"));
        return repository.findByLoanApplication(application)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCollateral(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Collateral not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
