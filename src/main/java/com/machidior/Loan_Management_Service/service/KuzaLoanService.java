package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.KuzaLoan;
import com.machidior.Loan_Management_Service.repo.KuzaLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KuzaLoanService {

    private final KuzaLoanRepository kuzaLoanRepository;
    private final FileStorageService fileStorageService;

    public KuzaLoan uploadLoanContract(String loanId, MultipartFile loanContract) throws IOException {

        KuzaLoan loan = kuzaLoanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with the given id is not found!"));

        String loanContractUrl = fileStorageService.saveLoanContractFiles(loanContract, loan.getId(), "LOAN-CONTRACT");
        loan.setLoanContractUrl(loanContractUrl);
        return kuzaLoanRepository.save(loan);
    }

    public KuzaLoan getKuzaLoan(String id){
        return kuzaLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business Loan not found!"));
    }
}
