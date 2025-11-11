package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanGuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanGuarantorResponse;
import com.machidior.Loan_Management_Service.mapper.BusinessLoanGuarantorMapper;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.BusinessLoanGuarantor;
import com.machidior.Loan_Management_Service.repo.BusinessLoanGuarantorRepository;
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
public class BusinessLoanGuarantorService {

    private final BusinessLoanRepository businessLoanRepository;
    private final BusinessLoanGuarantorRepository guarantorRepository;
    private final BusinessLoanGuarantorMapper guarantorMapper;

    public BusinessLoanGuarantorResponse addGuarantor(String loanId, BusinessLoanGuarantorRequest request) {
        BusinessLoan loan = businessLoanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Business loan not found with id: " + loanId));

        BusinessLoanGuarantor guarantor = guarantorMapper.toEntity(request, loan);
        guarantorRepository.save(guarantor);

        return guarantorMapper.toResponse(guarantor);
    }

    public List<BusinessLoanGuarantorResponse> getAllGuarantors(String loanId) {
        List<BusinessLoanGuarantor> guarantors = guarantorRepository.findByBusinessLoanId(loanId);
        return guarantors.stream().map(guarantorMapper::toResponse).collect(Collectors.toList());
    }

    public BusinessLoanGuarantorResponse approveGuarantor(Long guarantorId) {
        BusinessLoanGuarantor guarantor = guarantorRepository.findById(guarantorId)
                .orElseThrow(() -> new EntityNotFoundException("Guarantor not found with id: " + guarantorId));

        guarantor.setApproved(true);
        guarantorRepository.save(guarantor);

        return guarantorMapper.toResponse(guarantor);
    }

    public void deleteGuarantor(Long id) {
        if (!guarantorRepository.existsById(id)) {
            throw new EntityNotFoundException("Guarantor not found with id: " + id);
        }
        guarantorRepository.deleteById(id);
    }
}
