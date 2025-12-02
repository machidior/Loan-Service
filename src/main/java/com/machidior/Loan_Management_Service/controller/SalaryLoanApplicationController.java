package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.LoanApplicationApprovalRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.SalaryLoanApplicationResponse;
import com.machidior.Loan_Management_Service.model.SalaryLoan;
import com.machidior.Loan_Management_Service.service.SalaryLoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-application/salary")
@RequiredArgsConstructor
public class SalaryLoanApplicationController {

    private final SalaryLoanApplicationService service;

    @PostMapping("/apply")
    public ResponseEntity<SalaryLoanApplicationResponse> applySalaryLoan(@RequestBody SalaryLoanApplicationRequest request){
        return ResponseEntity.ok(service.applySalaryLoan(request));
    }

    @PostMapping("/approve/{applicationNumber}")
    public ResponseEntity<SalaryLoan> approveApplication(@PathVariable String applicationNumber, @RequestBody LoanApplicationApprovalRequest request){
        return ResponseEntity.ok(service.approveSalaryLoanApplication(applicationNumber,request));
    }

    @PostMapping("/reject/{applicationNumber}")
    public ResponseEntity<SalaryLoanApplicationResponse> rejectSalaryLoanApplication(@PathVariable String applicationNumber, @RequestBody String rejectionReason){
        return ResponseEntity.ok(service.rejectSalaryLoanApplication(applicationNumber,rejectionReason));
    }

    @PostMapping("/return/{applicationNumber}")
    public ResponseEntity<SalaryLoanApplicationResponse> returnSalaryLoanApplication(@PathVariable String applicationNumber, @RequestBody String reasonOfReturn){
        return ResponseEntity.ok(service.returnSalaryLoanApplication(applicationNumber,reasonOfReturn));
    }
}
