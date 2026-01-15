package com.machidior.Loan_Management_Service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machidior.Loan_Management_Service.dtos.request.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.response.DisbursementResponse;
import com.machidior.Loan_Management_Service.enums.DisbursementStatus;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.kafka.LoanDisbursedEventDTO;
import com.machidior.Loan_Management_Service.kafka.LoanEventProducer;
import com.machidior.Loan_Management_Service.mapper.DisbursementMapper;
import com.machidior.Loan_Management_Service.model.Disbursement;
import com.machidior.Loan_Management_Service.model.Loan;
import com.machidior.Loan_Management_Service.repo.DisbursementRepository;
import com.machidior.Loan_Management_Service.repo.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DisbursementService {
    private final DisbursementRepository disbursementRepository;
    private final LoanRepository loanRepository;
    private final DisbursementMapper mapper;
    private final LoanEventProducer loanEventProducer;

    public DisbursementResponse disburseLoan(DisbursementRequest request) throws JsonProcessingException {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan with given loan id is not found!"));

        if (loan.getLoanContractUrl()==null){
            throw new IllegalArgumentException("Loan contract is not uploaded!");
        }

        loan.getStatus().validateTransition(LoanStatus.DISBURSED);

        Disbursement disbursement = mapper.toEntity(request);
        disbursement.setProductId(loan.getProductId());
        disbursement.setProductName(loan.getProductName());
//        BUG: getUserID from the JWT token
        disbursement.setDisbursedBy("manager");
        disbursement.setAmountDisbursed(loan.getPrincipal());
        disbursement.setStatus(DisbursementStatus.DISBURSED);
        disbursement.setCustomerId(loan.getCustomerId());

        Disbursement savedDisbursement = disbursementRepository.save(disbursement);
        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursedOn( savedDisbursement.getDisbursementDate());
        Loan disbursedLoan = loanRepository.save(loan);

        LoanDisbursedEventDTO eventDTO = LoanDisbursedEventDTO.builder()
                .loanId(disbursedLoan.getId())
                .productId(disbursedLoan.getProductId())
                .productName(disbursedLoan.getProductName())
                .customerId(disbursedLoan.getCustomerId())
                .principal(disbursedLoan.getPrincipal())
                .interestRate(disbursedLoan.getInterestRate())
                .installmentFrequency(disbursedLoan.getInstallmentFrequency())
                .disbursedOn(disbursedLoan.getDisbursedOn())
                .build();

        loanEventProducer.sendLoanDisbursedEvent(eventDTO);

        return mapper.toResponse(savedDisbursement);
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
