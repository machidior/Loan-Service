package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.LoanApprovalResponse;
import com.machidior.Loan_Management_Service.service.LoanApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-approvals")
@RequiredArgsConstructor
public class LoanApprovalController {

    private final LoanApprovalService loanApprovalService;

    @GetMapping("/{loanId}")
    public ResponseEntity<List<LoanApprovalResponse>> getApprovalsByLoanId(@PathVariable String loanId) {
        return ResponseEntity.ok(loanApprovalService.getApprovalsByLoanId(loanId));
    }

    @GetMapping
    public ResponseEntity<List<LoanApprovalResponse>> getAllApprovals() {
        return ResponseEntity.ok(loanApprovalService.getAllApprovals());
    }
}
