package com.machidior.Loan_Management_Service.completion;

import com.machidior.Loan_Management_Service.enums.RequirementStatus;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RequirementCompletionServiceImpl
        implements RequirementCompletionService {

    private final LoanApplicationRequirementStatusRepository repository;
    private final RequirementMetadataExtractor extractor;

    public RequirementCompletionServiceImpl(
            LoanApplicationRequirementStatusRepository repository,
            RequirementMetadataExtractor extractor
    ) {
        this.repository = repository;
        this.extractor = extractor;
    }

    @Override
    public void initializeRequirements(
            LoanApplication application,
            Requirements requirements
    ) {

        Map<RequirementType, RequirementMetadata> meta =
                extractor.extract(requirements);

        for (Map.Entry<RequirementType, RequirementMetadata> e : meta.entrySet()) {

            RequirementStatus status =
                    (!e.getValue().enabled() || !e.getValue().mandatory())
                            ? RequirementStatus.NOT_REQUIRED
                            : RequirementStatus.PENDING;

            repository.save(
                    LoanApplicationRequirementStatus.builder()
                            .applicationNumber(application.getApplicationNumber())
                            .requirementType(e.getKey())
                            .status(status)
                            .build()
            );
        }
    }

    @Override
    public void markCompleted(
            String applicationNumber,
            RequirementType type
    ) {

        LoanApplicationRequirementStatus status =
                repository.findByApplicationNumberAndRequirementType(
                        applicationNumber, type
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Requirement not initialized: " + type
                        )
                );

        status.setStatus(RequirementStatus.COMPLETED);
    }

    @Override
    public boolean areMandatoryRequirementsCompleted(
            String applicationNumber,
            Requirements requirements
    ) {

        Map<RequirementType, RequirementMetadata> meta =
                extractor.extract(requirements);

        List<LoanApplicationRequirementStatus> statuses =
                repository.findAllByApplicationNumber(applicationNumber);

        for (LoanApplicationRequirementStatus s : statuses) {

            RequirementMetadata m = meta.get(s.getRequirementType());

            if (m != null &&
                    m.enabled() &&
                    m.mandatory() &&
                    s.getStatus() != RequirementStatus.COMPLETED) {

                return false;
            }
        }

        return true;
    }

}

