package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralResponse;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.BusinessLoanCollateralMapper;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.BusinessLoanApplication;
import com.machidior.Loan_Management_Service.model.BusinessLoanCollateral;
import com.machidior.Loan_Management_Service.repo.BusinessLoanApplicationRepository;
import com.machidior.Loan_Management_Service.repo.BusinessLoanCollateralRepository;
import com.machidior.Loan_Management_Service.repo.BusinessLoanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessLoanCollateralService {

    private final BusinessLoanApplicationRepository businessLoanApplicationRepository;
    private final BusinessLoanCollateralRepository collateralRepository;
    private final BusinessLoanCollateralMapper collateralMapper;

    public List<BusinessLoanCollateralResponse> getApplicationCollaterals(String applicationNumber){
        BusinessLoanApplication application = businessLoanApplicationRepository.findById(applicationNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Business loan not found!"));
        return collateralRepository.findByBusinessLoanApplication(application)
                .stream()
                .map(collateralMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteCollateral(Long id) {
        if (!collateralRepository.existsById(id)) {
            throw new EntityNotFoundException("Collateral not found with id: " + id);
        }
        collateralRepository.deleteById(id);
    }
}
