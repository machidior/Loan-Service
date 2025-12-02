package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.LoanApplicationApprovalResponse;
import com.machidior.Loan_Management_Service.service.LoanApplicationApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-approvals")
@RequiredArgsConstructor
public class LoanApplicationApprovalController {

    private final LoanApplicationApprovalService loanApplicationApprovalService;

    @GetMapping("/{applicationNumber}")
    public ResponseEntity<LoanApplicationApprovalResponse> getApprovalsByLoanId(@PathVariable String applicationNumber) {
        return ResponseEntity.ok(loanApplicationApprovalService.getApprovalsByApplicationNumber(applicationNumber));
    }

    @GetMapping
    public ResponseEntity<List<LoanApplicationApprovalResponse>> getAllApprovals() {
        return ResponseEntity.ok(loanApplicationApprovalService.getAllApprovals());
    }
}
