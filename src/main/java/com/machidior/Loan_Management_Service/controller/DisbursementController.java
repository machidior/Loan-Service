package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.DisbursementResponse;
import com.machidior.Loan_Management_Service.service.DisbursementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disbursements")
@RequiredArgsConstructor
public class DisbursementController {

    private final DisbursementService service;

    @PostMapping("/disburse/business")
    public ResponseEntity<DisbursementResponse> disburseBusinessLoan(@RequestBody DisbursementRequest request){
        return ResponseEntity.ok(service.disburseBusinessLoan(request));
    }

    @PostMapping("/disburse/salary")
    public ResponseEntity<DisbursementResponse> disburseSalaryLoan(@RequestBody DisbursementRequest request){
        return ResponseEntity.ok(service.disburseSalaryLoan(request));
    }

    @GetMapping("/loan-id/{loanId}")
    public ResponseEntity<DisbursementResponse> getDisbursementByLoanId(@PathVariable String loanId){
        return ResponseEntity.ok(service.getDisbursementByLoanId(loanId));
    }

    @DeleteMapping("/loan-id/{loanId}")
    public ResponseEntity<?> deleteLoanDisbursement(@PathVariable String loanId){
        service.deleteLoanDisbursement(loanId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<List<DisbursementResponse>> getAllDisbursements(){
        service.getAllDisbursements();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisbursementResponse> getDisbursementById(@PathVariable Long id){
        return ResponseEntity.ok(service.getDisbursementById(id));
    }
}
