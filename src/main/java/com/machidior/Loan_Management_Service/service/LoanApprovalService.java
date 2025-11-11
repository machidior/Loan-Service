package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.LoanApprovalResponse;
import com.machidior.Loan_Management_Service.mapper.LoanApprovalMapper;
import com.machidior.Loan_Management_Service.model.LoanApproval;
import com.machidior.Loan_Management_Service.repo.LoanApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanApprovalService {

    private final LoanApprovalRepository loanApprovalRepository;
    private final LoanApprovalMapper loanApprovalMapper;


    public List<LoanApprovalResponse> getApprovalsByLoanId(String loanId) {
        return loanApprovalRepository.findByLoanId(loanId)
                .stream()
                .map(loanApprovalMapper::toResponse)
                .collect(Collectors.toList());
    }


    public List<LoanApprovalResponse> getAllApprovals() {
        return loanApprovalRepository.findAll()
                .stream()
                .map(loanApprovalMapper::toResponse)
                .collect(Collectors.toList());
    }
}
