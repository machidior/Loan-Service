//package com.machidior.Loan_Management_Service.controller;
//
//import com.machidior.Loan_Management_Service.dtos.BusinessLoanRequest;
//import com.machidior.Loan_Management_Service.dtos.BusinessLoanResponse;
//import com.machidior.Loan_Management_Service.dtos.KuzaCapitalLoanRequest;
//import com.machidior.Loan_Management_Service.dtos.KuzaCapitalLoanResponse;
//import com.machidior.Loan_Management_Service.service.BusinessLoanService;
//import com.machidior.Loan_Management_Service.service.KuzaCapitalLoanService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/loans/kuza-capital")
//@RequiredArgsConstructor
//public class KuzaCapitalLoanController {
//
//    private final KuzaCapitalLoanService kuzaCapitalLoanService;
//
//    @PostMapping
//    public ResponseEntity<KuzaCapitalLoanResponse> createKuzaCapitalLoan(@RequestBody KuzaCapitalLoanRequest request) {
//        return ResponseEntity.ok(kuzaCapitalLoanService.createKuzaCapitalLoan(request));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<KuzaCapitalLoanResponse> getById(@PathVariable String id) {
//        return ResponseEntity.ok(kuzaCapitalLoanService.getKuzaCapitalLoanById(id));
//    }
//
//    @GetMapping("/application/{applicationNumber}")
//    public ResponseEntity<KuzaCapitalLoanResponse> getByApplicationNumber(@PathVariable String applicationNumber) {
//        return ResponseEntity.ok(kuzaCapitalLoanService.getKuzaCapitalLoanByApplicationNumber(applicationNumber));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<KuzaCapitalLoanResponse>> getAll() {
//        return ResponseEntity.ok(kuzaCapitalLoanService.getAllKuzaCapitalLoans());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<KuzaCapitalLoanResponse> update(@PathVariable String id, @RequestBody KuzaCapitalLoanRequest request) {
//        return ResponseEntity.ok(kuzaCapitalLoanService.updateKuzaCapitalLoan(id, request));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable String id) {
//        kuzaCapitalLoanService.deleteKuzaCapitalLoan(id);
//        return ResponseEntity.noContent().build();
//    }
//}
