package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.LoanApplicationApprovalResponse;
import com.machidior.Loan_Management_Service.mapper.LoanApplicationApprovalMapper;
import com.machidior.Loan_Management_Service.repo.LoanApplicationApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanApplicationApprovalService {

    private final LoanApplicationApprovalRepository loanApplicationApprovalRepository;
    private final LoanApplicationApprovalMapper loanApplicationApprovalMapper;


    public LoanApplicationApprovalResponse getApprovalsByApplicationNumber(String applicationNumber) {
        return loanApplicationApprovalMapper.toResponse(loanApplicationApprovalRepository.findByApplicationNumber(applicationNumber)
                .orElseThrow(() -> new RuntimeException("No loan approval with the given application number!")));
    }


    public List<LoanApplicationApprovalResponse> getAllApprovals() {
        return loanApplicationApprovalRepository.findAll()
                .stream()
                .map(loanApplicationApprovalMapper::toResponse)
                .collect(Collectors.toList());
    }
}
