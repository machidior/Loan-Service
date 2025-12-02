package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.DisbursementResponse;
import com.machidior.Loan_Management_Service.dtos.RepaymentScheduleItemDTO;
import com.machidior.Loan_Management_Service.enums.DisbursementStatus;
import com.machidior.Loan_Management_Service.enums.InstallmentFrequency;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.DisbursementMapper;
import com.machidior.Loan_Management_Service.mapper.RepaymentScheduleItemMapper;
import com.machidior.Loan_Management_Service.model.*;
import com.machidior.Loan_Management_Service.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DisbursementService{
    private final DisbursementRepository disbursementRepository;
    private final BusinessLoanRepository businessLoanRepository;
    private final KuzaLoanRepository kuzaLoanRepository;
    private final SalaryLoanRepository salaryLoanRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final DisbursementMapper mapper;
    private final RepaymentScheduleItemMapper repaymentScheduleItemMapper;
    private final RepaymentScheduleService repaymentScheduleService;

    public DisbursementResponse disburseBusinessLoan(DisbursementRequest request){
        BusinessLoan businessLoan = businessLoanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Business Loan with given loan id is not found!"));



        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setLoanProductType(LoanProductType.BUSINESS_PRODUCT);
        disbursement.setDisbursedBy("manager");
        disbursement.setAmountDisbursed(businessLoan.getPrincipal());
        disbursement.setStatus(DisbursementStatus.DISBURSED);
        disbursement.setCustomerId(businessLoan.getCustomerId());

        Disbursement savedDisbursement = disbursementRepository.save(disbursement);

        BigDecimal principal = businessLoan.getPrincipal();
        BigDecimal monthlyRate = businessLoan.getInterestRate().divide(BigDecimal.valueOf(100));
        BigDecimal loanFeeRate = businessLoan.getLoanFeeRate().divide(BigDecimal.valueOf(100));
        int termMonths = businessLoan.getTermMonths();
        InstallmentFrequency frequency = businessLoan.getInstallmentFrequency();

        List<RepaymentScheduleItemDTO> scheduleItems = repaymentScheduleService.generateFlatSchedule(principal,monthlyRate,loanFeeRate,termMonths, savedDisbursement.getDisbursementDate(),frequency);
        RepaymentSchedule schedule = new RepaymentSchedule();
        schedule.setLoanId(businessLoan.getId());
        List<RepaymentScheduleItem> scheduleItem = scheduleItems.stream()
                .map(item -> repaymentScheduleItemMapper.toEntity(item,schedule))
                .toList();
        schedule.setScheduleItems(scheduleItem);
        repaymentScheduleRepository.save(schedule);

        BigDecimal totalPayableAmount = BigDecimal.ZERO;
        for(int i = 0; i < scheduleItems.size(); i++){
            totalPayableAmount = totalPayableAmount.add(scheduleItems.get(i).getPaymentAmount());
        }
        businessLoan.setTotalPayableAmount(totalPayableAmount);
        businessLoan.setStatus(LoanStatus.DISBURSED);
        businessLoanRepository.save(businessLoan);

        return mapper.toResponse(savedDisbursement);
    }

    public DisbursementResponse disburseSalaryLoan(DisbursementRequest request){
        SalaryLoan loan = salaryLoanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Salary loan with the given id is not found!"));

        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setLoanProductType(LoanProductType.SALARY_PRODUCT);
        disbursement.setDisbursedBy("manager");
        disbursement.setAmountDisbursed(loan.getPrincipal());
        disbursement.setStatus(DisbursementStatus.DISBURSED);
        disbursement.setCustomerId(loan.getCustomerId());

        Disbursement savedDisbursement = disbursementRepository.save(disbursement);

        BigDecimal principal = loan.getPrincipal();
        BigDecimal monthlyRate = loan.getInterestRate().divide(BigDecimal.valueOf(100));
        BigDecimal loanFeeRate = loan.getLoanFeeRate().divide(BigDecimal.valueOf(100));
        int termMonths = loan.getTermMonths();
        InstallmentFrequency frequency = loan.getInstallmentFrequency();

        List<RepaymentScheduleItemDTO> scheduleItems = repaymentScheduleService.generateFlatSchedule(principal,monthlyRate,loanFeeRate,termMonths, savedDisbursement.getDisbursementDate(),frequency);
        RepaymentSchedule schedule = new RepaymentSchedule();
        schedule.setLoanId(loan.getId());
        List<RepaymentScheduleItem> scheduleItem = scheduleItems.stream()
                .map(item -> repaymentScheduleItemMapper.toEntity(item,schedule))
                .toList();
        schedule.setScheduleItems(scheduleItem);
        repaymentScheduleRepository.save(schedule);

        BigDecimal totalPayableAmount = BigDecimal.ZERO;
        for(int i = 0; i < scheduleItems.size(); i++){
            totalPayableAmount = totalPayableAmount.add(scheduleItems.get(i).getPaymentAmount());
        }
        loan.setTotalPayableAmount(totalPayableAmount);
        loan.setStatus(LoanStatus.DISBURSED);
        salaryLoanRepository.save(loan);

        return mapper.toResponse(savedDisbursement);
    }

    public DisbursementResponse disburseKuzaLoan(DisbursementRequest request){
        KuzaLoan kuzaLoan = kuzaLoanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Kuza Loan with given loan id is not found!"));



        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setLoanProductType(LoanProductType.KUZA_CAPITAL);
        disbursement.setDisbursedBy("manager");
        disbursement.setAmountDisbursed(kuzaLoan.getPrincipal());
        disbursement.setStatus(DisbursementStatus.DISBURSED);
        disbursement.setCustomerId(kuzaLoan.getCustomerId());

        Disbursement savedDisbursement = disbursementRepository.save(disbursement);

        BigDecimal principal = kuzaLoan.getPrincipal();
        BigDecimal monthlyRate = kuzaLoan.getInterestRate().divide(BigDecimal.valueOf(100));
        BigDecimal loanFeeRate = kuzaLoan.getLoanFeeRate().divide(BigDecimal.valueOf(100));
        int termMonths = kuzaLoan.getTermMonths();
        InstallmentFrequency frequency = kuzaLoan.getInstallmentFrequency();

        List<RepaymentScheduleItemDTO> scheduleItems = repaymentScheduleService.generateFlatSchedule(principal,monthlyRate,loanFeeRate,termMonths, savedDisbursement.getDisbursementDate(),frequency);
        RepaymentSchedule schedule = new RepaymentSchedule();
        schedule.setLoanId(kuzaLoan.getId());
        List<RepaymentScheduleItem> scheduleItem = scheduleItems.stream()
                .map(item -> repaymentScheduleItemMapper.toEntity(item,schedule))
                .toList();
        schedule.setScheduleItems(scheduleItem);
        repaymentScheduleRepository.save(schedule);

        BigDecimal totalPayableAmount = BigDecimal.ZERO;
        for(int i = 0; i < scheduleItems.size(); i++){
            totalPayableAmount = totalPayableAmount.add(scheduleItems.get(i).getPaymentAmount());
        }
        kuzaLoan.setTotalPayableAmount(totalPayableAmount);
        kuzaLoan.setStatus(LoanStatus.DISBURSED);
        kuzaLoanRepository.save(kuzaLoan);

        return mapper.toResponse(savedDisbursement);
    }

    public DisbursementResponse disburseStaffLoan(DisbursementRequest request){
        BusinessLoan businessLoan = businessLoanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff Loan with given loan id is not found!"));

        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setLoanProductType(LoanProductType.STAFF_PRODUCT);
        disbursement.setDisbursedBy("manager");

        businessLoan.setStatus(LoanStatus.DISBURSED);
        businessLoanRepository.save(businessLoan);

        return mapper.toResponse(disbursementRepository.save(disbursement));

    }

    public DisbursementResponse getDisbursementByLoanId(String loanId){
        Disbursement disbursement = disbursementRepository.findByLoanId(loanId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Disbursement with given loan id is not found!")
                );
        return mapper.toResponse(disbursement);
    }

    public void deleteLoanDisbursement(String loanId){
        Disbursement disbursement = disbursementRepository.findByLoanId(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Disbursement with given loan id is not found!"));
        RepaymentSchedule schedule = repaymentScheduleRepository.findByLoanId(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule with the given loan id is not found!"));
        repaymentScheduleRepository.deleteById(schedule.getId());
        disbursementRepository.deleteById(disbursement.getId());
    }

    public List<DisbursementResponse> getAllDisbursements(){
        List<Disbursement> disbursements = disbursementRepository.findAll();
        return disbursements.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public DisbursementResponse getDisbursementById(Long id){
        Disbursement disbursement = disbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disbursement with the given id is not found!"));
        return mapper.toResponse(disbursement);
    }

}
