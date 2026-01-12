package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationRejectionResponse;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.LoanApplicationRejectionMapper;
import com.machidior.Loan_Management_Service.model.LoanApplicationRejection;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRejectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanApplicationRejectionService {

    private final LoanApplicationRejectionRepository repository;
    private final LoanApplicationRejectionMapper mapper;

    public LoanApplicationRejectionResponse getLoanApplicationRejectionByApplicationNumber(String applicationNumber){
        LoanApplicationRejection rejection = repository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan rejection with the given application number is not found!"));
        return mapper.toResponse(rejection);
    }

    public List<LoanApplicationRejectionResponse> getAllLoanApplicationRejections(){
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteLoanApplicationRejectionByApplicationNumber(String applicationNumber){
        LoanApplicationRejection rejection = repository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan rejection with the given application number is not found!"));
        repository.deleteById(rejection.getId());
    }
}
