package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.model.KuzaLoan;
import com.machidior.Loan_Management_Service.service.KuzaLoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-application/kuza")
@RequiredArgsConstructor
public class KuzaLoanApplicationController {

    private final KuzaLoanApplicationService service;

    @PostMapping("/apply")
    public ResponseEntity<KuzaLoanApplicationResponse> applyKuzaLoan(@RequestBody KuzaLoanApplicationRequest request){
        return ResponseEntity.ok(service.applyKuzaLoan(request));
    }

    @PostMapping("/approve/{applicationNumber}")
    public ResponseEntity<KuzaLoan> approveApplication(@PathVariable String applicationNumber, @RequestBody LoanApplicationApprovalRequest request){
        return ResponseEntity.ok(service.approveKuzaLoanApplication(applicationNumber,request));
    }

    @PostMapping("/reject/{applicationNumber}")
    public ResponseEntity<KuzaLoanApplicationResponse> rejectKuzaLoanApplication(@PathVariable String applicationNumber, @RequestBody String rejectionReason){
        return ResponseEntity.ok(service.returnKuzaLoanApplication(applicationNumber,rejectionReason));
    }

    @PostMapping("/return/{applicationNumber}")
    public ResponseEntity<KuzaLoanApplicationResponse> returnKuzaLoanApplication(@PathVariable String applicationNumber, @RequestBody String reasonOfReturn){
        return ResponseEntity.ok(service.returnKuzaLoanApplication(applicationNumber,reasonOfReturn));
    }
}
