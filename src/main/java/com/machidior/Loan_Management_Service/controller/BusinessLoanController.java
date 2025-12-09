package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.service.BusinessLoanService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/business-loan")
@RequiredArgsConstructor
public class BusinessLoanController {

    private final BusinessLoanService businessLoanService;

    @PostMapping(path = "/upload/loan-contract/{loanId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusinessLoan> uploadLoanContract(@PathVariable String loanId, @RequestParam("loanContract") MultipartFile loanContract) throws IOException {
        return ResponseEntity.ok(businessLoanService.uploadLoanContract(loanId,loanContract));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessLoan> getBusinessLoan(@PathVariable String id){
        return ResponseEntity.ok(businessLoanService.getBusinessLoan(id));
    }
}
