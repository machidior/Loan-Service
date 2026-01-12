package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.response.GuarantorResponse;
import com.machidior.Loan_Management_Service.exception.ResourceNotFoundException;
import com.machidior.Loan_Management_Service.mapper.GuarantorMapper;
import com.machidior.Loan_Management_Service.model.Guarantor;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.repo.GuarantorRepository;
import com.machidior.Loan_Management_Service.repo.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuarantorService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final GuarantorRepository guarantorRepository;
    private final GuarantorMapper mapper;


    public GuarantorResponse getLoanGuarantor(String applicationNumber){
        LoanApplication application = loanApplicationRepository.findById(applicationNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Loan Application not found!"));

        Guarantor guarantor = guarantorRepository.findByLoanApplication(application)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantor not found with application number: " + applicationNumber));

        return mapper.toResponse(guarantor);
    }

    public GuarantorResponse approveGuarantor(Long guarantorId) {
        Guarantor guarantor = guarantorRepository.findById(guarantorId)
                .orElseThrow(() -> new ResourceNotFoundException("Guarantor not found with id: " + guarantorId));

        guarantor.setApproved(true);
        guarantorRepository.save(guarantor);

        return mapper.toResponse(guarantor);
    }

    public void deleteGuarantor(Long id) {
        if (!guarantorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guarantor not found with id: " + id);
        }
        guarantorRepository.deleteById(id);
    }
}
