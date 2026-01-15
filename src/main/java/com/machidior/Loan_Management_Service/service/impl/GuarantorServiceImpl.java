package com.machidior.Loan_Management_Service.service.impl;

import com.machidior.Loan_Management_Service.completion.RequirementCompletionService;
import com.machidior.Loan_Management_Service.dtos.request.GuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.response.GuarantorResponse;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.data.GuarantorItemData;
import com.machidior.Loan_Management_Service.evaluator.data.GuarantorRequirementData;
import com.machidior.Loan_Management_Service.evaluator.requirement.GuarantorRequirementEvaluator;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.mapper.GuarantorMapper;
import com.machidior.Loan_Management_Service.model.requirement.Guarantor;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.repo.GuarantorRepository;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRepository;
import com.machidior.Loan_Management_Service.service.FileStorageService;
import com.machidior.Loan_Management_Service.service.GuarantorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuarantorServiceImpl implements GuarantorService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final GuarantorRepository guarantorRepository;
    private final GuarantorMapper mapper;
    private final FileStorageService fileStorageService;
    private final GuarantorRequirementEvaluator guarantorRequirementEvaluator;
    private final ProductConfigurationsClient productConfigurationsClient;
    private final RequirementCompletionService requirementCompletionService;




    @Transactional
    @Override
    public RequirementSubmissionResponse saveGuarantor(
            String applicationNumber,
            List<MultipartFile> passportPhotos,
            List<MultipartFile> identificationCards,
            List<MultipartFile> guarantorConsents,
            List<MultipartFile> incomeProofs,
            List<GuarantorRequest> guarantorRequests
    ) throws IOException {
        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
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
            Guarantor guarantor = mapper.toEntity(req, application);
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


        guarantorRepository.saveAll(guarantors);
        requirementCompletionService.markCompleted(
                applicationNumber,
                RequirementType.GUARANTOR
        );

        return new RequirementSubmissionResponse(true, List.of());

    }

    @Override
    public GuarantorResponse getLoanGuarantor(String applicationNumber){
        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Loan Application not found!"));

        Guarantor guarantor = guarantorRepository.findByLoanApplication(application)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantor not found with application number: " + applicationNumber));

        return mapper.toResponse(guarantor);
    }

    @Override
    public GuarantorResponse approveGuarantor(Long guarantorId) {
        Guarantor guarantor = guarantorRepository.findById(guarantorId)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantor not found with id: " + guarantorId));

        guarantor.setApproved(true);
        guarantorRepository.save(guarantor);

        return mapper.toResponse(guarantor);
    }

    @Override
    public void deleteGuarantor(Long id) {
        if (!guarantorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guarantor not found with id: " + id);
        }
        guarantorRepository.deleteById(id);
    }
}
