package com.machidior.Loan_Management_Service.service.impl;

import com.machidior.Loan_Management_Service.completion.RequirementCompletionService;
import com.machidior.Loan_Management_Service.dtos.request.EmploymentRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.RequirementEvaluationResult;
import com.machidior.Loan_Management_Service.evaluator.data.EmploymentRequirementData;
import com.machidior.Loan_Management_Service.evaluator.requirement.EmploymentRequirementEvaluator;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.mapper.EmploymentMapper;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.model.requirement.EmploymentDetails;
import com.machidior.Loan_Management_Service.repo.EmploymentDetailsRepository;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRepository;
import com.machidior.Loan_Management_Service.service.EmploymentService;
import com.machidior.Loan_Management_Service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentServiceImpl implements EmploymentService {

    private final FileStorageService fileStorageService;
    private final ProductConfigurationsClient productConfigurationsClient;
    private final LoanApplicationRepository loanApplicationRepository;
    private final EmploymentDetailsRepository repository;
    private final EmploymentMapper mapper;
    private final EmploymentRequirementEvaluator employmentRequirementEvaluator;
    private final RequirementCompletionService requirementCompletionService;

    @Transactional
    @Override
    public RequirementSubmissionResponse saveEmploymentDetails(
            String applicationNumber,
            MultipartFile paySlip,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile contract,
            MultipartFile payrollDeduction,
            EmploymentRequest request
    ) throws IOException {
        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));

        EmploymentRequirementData data = new EmploymentRequirementData();
        data.setJobContractProvided(contract != null);
        data.setPaySlipProvided(paySlip != null);
        data.setMonthsEmployed(request.getEmploymentDurationMonths());
        data.setPayrollDeductionProvided(payrollDeduction != null);
        data.setNetMonthlyIncome(request.getNetMonthlyIncome());


        Requirements requirements = productConfigurationsClient.getVersionRequirements(application.getProductVersionId());

        RequirementEvaluationResult result = employmentRequirementEvaluator.evaluate(
                data,
                requirements.getEmploymentRequirement()
        );

        if (!result.isPassed()) {
            return new RequirementSubmissionResponse(
                    false,
                    result.getViolations()
            );
        }


        assert paySlip != null;
        String paySlipUrl = fileStorageService.saveJobFiles(
                paySlip,
                application.getApplicationNumber(),
                "PAY-SLIP"
        );
        assert insuranceComprehensiveCover != null;
        String insuranceComprehensiveCoverUrl = fileStorageService.saveJobFiles(
                insuranceComprehensiveCover,
                application.getApplicationNumber(),
                "INSURANCE-COVER"
        );
        assert contract != null;
        String contractUrl = fileStorageService.saveJobFiles(
                contract,
                application.getApplicationNumber(),
                "JOB-CONTRACT"
        );

        assert payrollDeduction != null;
        String payrollDeductionUrl = fileStorageService.saveJobFiles(
                payrollDeduction,
                application.getApplicationNumber(),
                "PAYROLL-DEDUCTION"
        );

        EmploymentDetails details = mapper.toEntity(request, application);

        details.setPaySlipUrl(paySlipUrl);
        details.setPayslipProvided(true);
        details.setInsuranceComprehensiveCoverUrl(insuranceComprehensiveCoverUrl);
        details.setInsuranceComprehensiveCoverProvided(true);
        details.setContractUrl(contractUrl);
        details.setContractProvided(true);
        details.setPayrollDeductionUrl(payrollDeductionUrl);
        details.setPayrollDeductionProvided(true);
        repository.save(details);
        requirementCompletionService.markCompleted(
                applicationNumber,
                RequirementType.EMPLOYMENT_DETAILS
        );

        return new RequirementSubmissionResponse(true, List.of());
    }

}
