package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.DisbursementResponse;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.DisbursementMapper;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.Disbursement;
import com.machidior.Loan_Management_Service.repo.BusinessLoanRepository;
import com.machidior.Loan_Management_Service.repo.DisbursementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DisbursementService{
    private final DisbursementRepository disbursementRepository;
    private final BusinessLoanRepository businessLoanRepository;
    private final DisbursementMapper mapper;

    public DisbursementResponse disburseBusinessLoan(DisbursementRequest request){
        BusinessLoan businessLoan = businessLoanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Business Loan with given loan id is not found!"));

        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setLoanProductType(LoanProductType.BUSINESS_PRODUCT);
        disbursement.setDisbursedBy("manager");

        businessLoan.setStatus(LoanStatus.DISBURSED);
        businessLoanRepository.save(businessLoan);

        return mapper.toResponse(disbursementRepository.save(disbursement));

    }

    public DisbursementResponse disburseSalaryLoan(DisbursementRequest request){
        BusinessLoan businessLoan = businessLoanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Salary Loan with given loan id is not found!"));

        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setLoanProductType(LoanProductType.SALARY_PRODUCT);
        disbursement.setDisbursedBy("manager");

        businessLoan.setStatus(LoanStatus.DISBURSED);
        businessLoanRepository.save(businessLoan);

        return mapper.toResponse(disbursementRepository.save(disbursement));

    }

    public DisbursementResponse disburseKuzaCapitalLoan(DisbursementRequest request){
        BusinessLoan businessLoan = businessLoanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Kuza-Capital Loan with given loan id is not found!"));

        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setLoanProductType(LoanProductType.KUZA_CAPITAL);
        disbursement.setDisbursedBy("manager");


        businessLoan.setStatus(LoanStatus.DISBURSED);
        businessLoanRepository.save(businessLoan);

        return mapper.toResponse(disbursementRepository.save(disbursement));

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
}
