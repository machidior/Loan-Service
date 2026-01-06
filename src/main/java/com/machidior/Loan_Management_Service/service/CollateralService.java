package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.CollateralResponse;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.CollateralMapper;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.repo.CollateralRepository;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollateralService {

    private final CollateralRepository repository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final CollateralMapper mapper;

    public List<CollateralResponse> getLoanCollaterals(String applicationNumber){
        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Loan Application not found!"));
        return repository.findByLoanApplication(application)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteCollateral(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Collateral not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
