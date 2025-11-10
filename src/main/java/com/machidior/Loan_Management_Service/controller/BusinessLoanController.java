package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanResponse;
import com.machidior.Loan_Management_Service.service.BusinessLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans/business")
@RequiredArgsConstructor
public class BusinessLoanController {

    private final BusinessLoanService businessLoanService;

    @PostMapping
    public ResponseEntity<BusinessLoanResponse> createBusinessLoan(@RequestBody BusinessLoanRequest request) {
        return ResponseEntity.ok(businessLoanService.createBusinessLoan(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessLoanResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(businessLoanService.getBusinessLoanById(id));
    }

    @GetMapping("/application/{applicationNumber}")
    public ResponseEntity<BusinessLoanResponse> getByApplicationNumber(@PathVariable String applicationNumber) {
        return ResponseEntity.ok(businessLoanService.getBusinessLoanByApplicationNumber(applicationNumber));
    }

    @GetMapping
    public ResponseEntity<List<BusinessLoanResponse>> getAll() {
        return ResponseEntity.ok(businessLoanService.getAllBusinessLoans());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessLoanResponse> update(@PathVariable String id, @RequestBody BusinessLoanRequest request) {
        return ResponseEntity.ok(businessLoanService.updateBusinessLoan(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        businessLoanService.deleteBusinessLoan(id);
        return ResponseEntity.noContent().build();
    }
}
