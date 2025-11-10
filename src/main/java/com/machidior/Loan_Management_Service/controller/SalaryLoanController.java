package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.SalaryLoanRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanResponse;
import com.machidior.Loan_Management_Service.enums.LoanStatus;
import com.machidior.Loan_Management_Service.service.SalaryLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans/salary")
@RequiredArgsConstructor
public class SalaryLoanController {

    private final SalaryLoanService salaryLoanService;

    @PostMapping("/apply")
    public ResponseEntity<SalaryLoanResponse> applyForLoan(@RequestBody SalaryLoanRequest request) {
        return ResponseEntity.ok(salaryLoanService.applyForLoan(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryLoanResponse> getLoanById(@PathVariable String id) {
        return ResponseEntity.ok(salaryLoanService.getLoanById(id));
    }

    @GetMapping
    public ResponseEntity<List<SalaryLoanResponse>> getAllLoans() {
        return ResponseEntity.ok(salaryLoanService.getAllLoans());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SalaryLoanResponse> updateStatus(
            @PathVariable String id,
            @RequestParam LoanStatus status) {
        return ResponseEntity.ok(salaryLoanService.updateStatus(id, status));
    }
}
