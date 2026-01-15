package com.machidior.Loan_Management_Service.service.impl;

import com.machidior.Loan_Management_Service.completion.RequirementCompletionService;
import com.machidior.Loan_Management_Service.dtos.request.*;
import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.enums.LoanApplicationStatus;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.feign.ProductConfigurationsClient;
import com.machidior.Loan_Management_Service.feign.policy.Policies;
import com.machidior.Loan_Management_Service.feign.policy.ProductTerms;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import com.machidior.Loan_Management_Service.mapper.*;
import com.machidior.Loan_Management_Service.model.*;
import com.machidior.Loan_Management_Service.repo.*;
import com.machidior.Loan_Management_Service.service.LoanApplicationService;
import com.machidior.Loan_Management_Service.service.LoanCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final LoanApplicationMapper mapper;
    private final LoanCalculationService calculator;
    private final LoanRepository loanRepository;
    private final LoanApplicationApprovalRepository loanApplicationApprovalRepository;
    private final LoanApplicationRejectionRepository loanApplicationRejectionRepository;
    private final LoanApplicationReturnRepository loanApplicationReturnRepository;

    private final ProductConfigurationsClient productConfigurationsClient;
    private final RequirementCompletionService requirementCompletionService;


    @Override
    public LoanApplicationResponse createLoanApplicationDetails(ApplicationDetails request) {

        Policies policies =
                productConfigurationsClient.getVersionPolicies(
                        request.getProductVersionId()
                );

        ProductTerms terms = policies.getTermsPolicy();

        BigDecimal requestedAmount = request.getAmountRequested();

        if (requestedAmount.compareTo(terms.getMinLoanAmount()) < 0 ||
                requestedAmount.compareTo(terms.getMaxLoanAmount()) > 0) {

            throw new IllegalArgumentException(
                    "Requested amount is outside the allowed range: " +
                            terms.getMinLoanAmount() + " - " + terms.getMaxLoanAmount()
            );
        }

        validateTenure(request.getTermTenure(), terms);

        if (terms.getAllowedInstallmentFrequencies() == null ||
                !terms.getAllowedInstallmentFrequencies().contains(request.getInstallmentFrequency())) {

            throw new IllegalArgumentException(
                    "Installment frequency " + request.getInstallmentFrequency() +
                            " is not allowed for this product version"
            );
        }

        if (isFirstLoanForCustomer(request.getCustomerId(), request.getProductId())) {
            if (requestedAmount.compareTo(terms.getFirstTimeBorrowerMaxAmount()) > 0) {
                throw new IllegalArgumentException(
                        "Requested amount exceeds the maximum allowed for first-time borrowers: " +
                                terms.getFirstTimeBorrowerMaxAmount()
                );
            }
        }


        LoanApplication application = mapper.toEntity(request);

        application.setStatus(LoanApplicationStatus.DRAFTED);
        application.setAmountApproved(null);
        application.setIsRead(false);
        LoanApplication savedApplication = repository.save(application);

        Requirements requirements = productConfigurationsClient.getVersionRequirements(savedApplication.getProductVersionId());
        requirementCompletionService.initializeRequirements(savedApplication, requirements);

        return mapper.toResponse(savedApplication);
    }

    private boolean isFirstLoanForCustomer(String customerID, Long productID) {
        List<LoanApplication> applications = repository.findByCustomerIdAndProductId(customerID, productID);
        return applications.isEmpty();
    }

    private void validateTenure(Integer requestedTenure, ProductTerms terms) {

        switch (terms.getTenureUnit()) {

            case MONTHS -> {
                if (requestedTenure < terms.getMinTenure() ||
                        requestedTenure > terms.getMaxTenure()) {

                    throw new IllegalArgumentException(
                            "Requested tenure is outside allowed range: " +
                                    terms.getMinTenure() + " - " +
                                    terms.getMaxTenure() + " months"
                    );
                }
            }

            case WEEKS -> {
                int weeks = requestedTenure * 4;

                if (weeks < terms.getMinTenure() ||
                        weeks > terms.getMaxTenure()) {

                    throw new IllegalArgumentException(
                            "Requested tenure is outside allowed range: " +
                                    terms.getMinTenure() + " - " +
                                    terms.getMaxTenure() + " weeks"
                    );
                }
            }

            case DAYS -> {
                int days = requestedTenure * 30;

                if (days < terms.getMinTenure() ||
                        days > terms.getMaxTenure()) {

                    throw new IllegalArgumentException(
                            "Requested tenure is outside allowed range: " +
                                    terms.getMinTenure() + " - " +
                                    terms.getMaxTenure() + " days"
                    );
                }
            }

            default -> throw new IllegalStateException(
                    "Unsupported tenure unit: " + terms.getTenureUnit()
            );
        }
    }


    @Override
    public LoanApplicationResponse submitLoan(String applicationNumber){

            LoanApplication application = repository.findById(applicationNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("Loan Application with application number " +applicationNumber + " is not found!"));

            Requirements requirements = productConfigurationsClient.getVersionRequirements(application.getProductVersionId());

            boolean completed = requirementCompletionService.areMandatoryRequirementsCompleted(
                    application.getApplicationNumber(),
                    requirements
            );

            if (!completed) {
                throw new IllegalArgumentException("Some mandatory requirements are not completed.");
            }
            application.setStatus(LoanApplicationStatus.SUBMITTED);
            return mapper.toResponse(repository.save(application));
        }



    @Override
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
//        ToDo: implement total payable amount of the application from the product configurations [ interest calculation method used ]
        loan.setTotalPayableAmount(null); // should not be null
        loan.setProductId(approvedApplication.getProductId());
        loan.setProductName(approvedApplication.getProductName());
        loan.setTermTenure(approvedApplication.getTermTenure());
        loan.setTenureUnit(approvedApplication.getTenureUnit());
        loan.setLoanContractUrl(null);
        loan.setAppliedOn(approvedApplication.getCreatedAt());
        loan.setApprovedOn(LocalDateTime.now());
        loan.setStatus(LoanStatus.PENDING);
        loan.setInstallmentFrequency(approvedApplication.getInstallmentFrequency());

        return loanRepository.save(loan);
    }

    @Override
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

    @Override
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

    @Override
    public LoanApplicationResponse getLoanApplication(String applicationNumber) {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(()->new ResourceNotFoundException("There is no loan application with application number: " + applicationNumber));
        return mapper.toResponse(application);
    }

    @Override
    public List<LoanApplicationResponse> getAllLoanApplications() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLoanApplication(String applicationNumber) {
        LoanApplication application = repository.findById(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("There is no loan application with application number: " + applicationNumber));
        repository.delete(application);
    }

}
