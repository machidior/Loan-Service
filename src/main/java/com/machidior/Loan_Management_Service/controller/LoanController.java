package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.model.Loan;
import com.machidior.Loan_Management_Service.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;

    @PostMapping(path = "/upload/loan-contract/{loanId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Loan> uploadLoanContract(@PathVariable String loanId, @RequestParam("loanContract") MultipartFile loanContract) throws IOException {
        return ResponseEntity.ok(service.uploadLoanContract(loanId,loanContract));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getBusinessLoan(@PathVariable String id){
        return ResponseEntity.ok(service.getLoan(id));
    }
}
