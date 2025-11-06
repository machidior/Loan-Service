package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.dtos.LoanApprovalRequest;
import com.machidior.Loan_Management_Service.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanApplicationResponse> apply(@RequestBody LoanApplicationRequest request) {
        return ResponseEntity.ok(loanService.applyForLoan(request));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<LoanApplicationResponse> approve(@PathVariable Long id, @RequestBody LoanApprovalRequest request) {
        return ResponseEntity.ok(loanService.approveLoan(id, request));
    }

    @PostMapping("/{id}/disburse")
    public ResponseEntity<LoanApplicationResponse> disburse(@PathVariable Long id, @RequestBody DisbursementRequest request) {
        return ResponseEntity.ok(loanService.disburseLoan(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponse> getLoan(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getLoansByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(loanService.getLoansByCustomer(customerId));
    }
}
