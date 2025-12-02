package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.LoanApplicationRejectionResponse;
import com.machidior.Loan_Management_Service.service.LoanApplicationRejectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-applications/rejection")
@RequiredArgsConstructor
public class LoanApplicationRejectionController {

    private final LoanApplicationRejectionService service;

    @GetMapping("/application-number/{applicationNumber}")
    public ResponseEntity<LoanApplicationRejectionResponse> getLoanApplicationRejectionByApplicationNumber(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.getLoanApplicationRejectionByApplicationNumber(applicationNumber));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LoanApplicationRejectionResponse>> getAllRejections(){
        return ResponseEntity.ok(service.getAllLoanApplicationRejections());
    }

    @DeleteMapping("/delete-by-application-number/{applicationNumber}")
    public ResponseEntity<?> deleteLoanApplicationRejectionByApplicationNumber(@PathVariable String applicationNumber){
        service.deleteLoanApplicationRejectionByApplicationNumber(applicationNumber);
        return ResponseEntity.ok("Loan application rejection deleted successfully!");
    }
}
