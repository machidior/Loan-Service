package com.machidior.Loan_Management_Service.service;


import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.model.KuzaLoan;
import com.machidior.Loan_Management_Service.model.SalaryLoan;
import com.machidior.Loan_Management_Service.repo.SalaryLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Transactional
public class SalaryLoanService {

    private final SalaryLoanRepository salaryLoanRepository;
    private final FileStorageService fileStorageService;

    public SalaryLoan uploadLoanContract(String loanId, MultipartFile loanContract) throws IOException {

        SalaryLoan loan = salaryLoanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with the given id is not found!"));

        String loanContractUrl = fileStorageService.saveLoanContractFiles(loanContract, loan.getId(), "LOAN-CONTRACT");
        loan.setLoanContractUrl(loanContractUrl);
        return salaryLoanRepository.save(loan);
    }

    public SalaryLoan getSalaryLoan(String id){
        return salaryLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary Loan not found!"));
    }

}
