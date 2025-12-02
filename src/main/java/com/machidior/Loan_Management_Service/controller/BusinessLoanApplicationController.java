package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.service.BusinessLoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-application/business")
@RequiredArgsConstructor
public class BusinessLoanApplicationController {

    private final BusinessLoanApplicationService service;

    @PostMapping("/apply")
    public ResponseEntity<BusinessLoanApplicationResponse> applyBusinessLoan(@RequestBody BusinessLoanApplicationRequest request){
        return ResponseEntity.ok(service.applyBusinessLoan(request));
    }

    @PostMapping("/approve/{applicationNumber}")
    public ResponseEntity<BusinessLoan> approveApplication(@PathVariable String applicationNumber, @RequestBody LoanApplicationApprovalRequest request){
        return ResponseEntity.ok(service.approveBusinessLoanApplication(applicationNumber,request));
    }

    @PostMapping("/reject/{applicationNumber}")
    public ResponseEntity<BusinessLoanApplicationResponse> rejectBusinessLoanApplication(@PathVariable String applicationNumber, @RequestBody String rejectionReason){
        return ResponseEntity.ok(service.rejectBusinessLoanApplication(applicationNumber,rejectionReason));
    }

    @PostMapping("/return/{applicationNumber}")
    public ResponseEntity<BusinessLoanApplicationResponse> returnBusinessLoanApplication(@PathVariable String applicationNumber, @RequestBody String reasonOfReturn){
        return ResponseEntity.ok(service.returnBusinessLoanApplication(applicationNumber,reasonOfReturn));
    }
}
