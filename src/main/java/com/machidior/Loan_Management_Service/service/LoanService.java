package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.dtos.LoanApprovalRequest;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.generator.RepaymentScheduleGenerator;
import com.machidior.Loan_Management_Service.mapper.LoanMapper;
import com.machidior.Loan_Management_Service.model.*;
import com.machidior.Loan_Management_Service.repo.*;
import com.machidior.Loan_Management_Service.rules.LoanProductRules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService{

    private final LoanApplicationRepository loanRepository;
    private final CollateralRepository collateralRepository;
    private final GuarantorRepository guarantorRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final LoanEventLogRepository logRepository;

    public LoanApplicationResponse applyForLoan(LoanApplicationRequest request) {
        LoanProductType type = LoanProductType.valueOf(request.getLoanProductType());
        LoanProductRules.ProductRule rule = productRules.getRule(type);

        // ✅ 1. Amount validation
        if (request.getAmountRequested().doubleValue() < rule.minAmount() ||
                request.getAmountRequested().doubleValue() > rule.maxAmount()) {
            throw new RuntimeException("Requested amount not within product limits.");
        }

        // ✅ 2. Term validation
        if (request.getTermMonths() < rule.minTermMonths() || request.getTermMonths() > rule.maxTermMonths()) {
            throw new RuntimeException("Loan term not within product limits.");
        }

        // ✅ 3. Collateral & Guarantor requirements
        if (rule.requiresCollateral() && (request.getCollaterals() == null || request.getCollaterals().isEmpty())) {
            throw new RuntimeException("This product requires collateral.");
        }

        if (rule.requiresGuarantor() && (request.getGuarantors() == null || request.getGuarantors().isEmpty())) {
            throw new RuntimeException("This product requires at least one guarantor.");
        }

        if (rule.requiresGroup() && request.getCustomerId() == null) {
            throw new RuntimeException("This product requires a registered client group.");
        }

        // ✅ 4. Save Application
        LoanApplication loan = LoanMapper.toEntity(request);
        loan.setApplicationNumber("APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        loan.setStatus(LoanStatus.PENDING);
        loanRepository.save(loan);

        // ✅ 5. Save Guarantors and Collaterals
        if (request.getCollaterals() != null) {
            request.getCollaterals().forEach(c -> {
                Collateral col = new Collateral();
                col.setType(c.getType());
                col.setDescription(c.getDescription());
                col.setDocument(c.getDocument());
                col.setValuation(c.getValuation());
                col.setLoanApplication(loan);
                collateralRepository.save(col);
            });
        }

        if (request.getGuarantors() != null) {
            request.getGuarantors().forEach(g -> {
                Guarantor guarantor = new Guarantor();
                guarantor.setName(g.getName());
                guarantor.setRelationship(g.getRelationship());
                guarantor.setPhoneNumber(g.getPhoneNumber());
                guarantor.setNationalId(g.getNationalId());
                guarantor.setAddress(g.getAddress());
                guarantor.setDocument(g.getDocument());
                guarantor.setLoanApplication(loan);
                guarantorRepository.save(guarantor);
            });
        }

        return LoanMapper.toResponse(loan);
    }


    public LoanApplicationResponse approveLoan(Long id, LoanApprovalRequest request) {
        LoanApplication loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Loan already processed or approved.");
        }

        loan.setAmountApproved(request.getApprovedAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setStatus(LoanStatus.APPROVED);
        loanRepository.save(loan);

        // ✅ Generate repayment schedule
        List<RepaymentSchedule> schedule = RepaymentScheduleGenerator.generate(loan);
        repaymentScheduleRepository.saveAll(schedule);

        return LoanMapper.toResponse(loan);
    }


    public LoanApplicationResponse disburseLoan(Long id, DisbursementRequest request) {
        LoanApplication loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("Only approved loans can be disbursed.");
        }

        loan.setStatus(LoanStatus.DISBURSED);
        loanRepository.save(loan);

        // Optional: Log event
        LoanEventLog log = new LoanEventLog();
        log.setEventType("DISBURSEMENT");
        log.setCreatedBy(request.getDisbursedBy());
        log.setPayload("Loan disbursed to account " + request.getAccountNumber());
        log.setLoanApplication(loan);
        logRepository.save(log);

        return LoanMapper.toResponse(loan);
    }


    public LoanApplicationResponse getLoanById(Long id) {
        return loanRepository.findById(id)
                .map(LoanMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    public List<LoanApplicationResponse> getLoansByCustomer(String customerId) {
        return loanRepository.findByCustomerId(customerId)
                .stream()
                .map(LoanMapper::toResponse)
                .collect(Collectors.toList());
    }

    public LoanApplicationResponse terminateLoan(Long id, String reason) {
        LoanApplication loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(LoanStatus.TERMINATED);
        loan.setRemarks(reason);
        loanRepository.save(loan);
        return LoanMapper.toResponse(loan);
    }

}
