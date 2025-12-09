package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.model.KuzaLoan;
import com.machidior.Loan_Management_Service.service.KuzaLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/kuza-loans")
@RequiredArgsConstructor
public class KuzaLoanController {

    private final KuzaLoanService kuzaLoanService;

    @PostMapping(path = "/upload/loan-contract/{loanId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KuzaLoan> uploadLoanContract(@PathVariable String loanId, @RequestParam("loanContract") MultipartFile loanContract) throws IOException {
        return ResponseEntity.ok(kuzaLoanService.uploadLoanContract(loanId,loanContract));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KuzaLoan> getKuzaLoan(@PathVariable String id){
        return ResponseEntity.ok(kuzaLoanService.getKuzaLoan(id));
    }
}
