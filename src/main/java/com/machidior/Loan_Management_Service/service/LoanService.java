package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.model.Loan;
import com.machidior.Loan_Management_Service.repo.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final LoanRepository repository;
    private final FileStorageService fileStorageService;

    public Loan uploadLoanContract(String loanId, MultipartFile loanContract) throws IOException {

        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with the given id is not found!"));

        String loanContractUrl = fileStorageService.saveLoanContractFiles(loanContract, loan.getId(), "LOAN-CONTRACT");
        loan.setLoanContractUrl(loanContractUrl);
        return repository.save(loan);
    }

    public Loan getLoan(String id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business Loan not found!"));
    }
}
