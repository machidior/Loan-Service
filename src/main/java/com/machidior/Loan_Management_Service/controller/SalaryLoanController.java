package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.model.KuzaLoan;
import com.machidior.Loan_Management_Service.model.SalaryLoan;
import com.machidior.Loan_Management_Service.service.SalaryLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/salary-loans")
@RequiredArgsConstructor
public class SalaryLoanController {

    private final SalaryLoanService service;

    @PostMapping(path = "/upload/loan-contract/{loanId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalaryLoan> uploadLoanContract(@PathVariable String loanId, @RequestParam("loanContract") MultipartFile loanContract) throws IOException {
        return ResponseEntity.ok(service.uploadLoanContract(loanId,loanContract));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryLoan> getSalaryLoan(@PathVariable String id) {
        return ResponseEntity.ok(service.getSalaryLoan(id));
    }
}
