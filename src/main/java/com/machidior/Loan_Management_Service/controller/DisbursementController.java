package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.DisbursementRequest;
import com.machidior.Loan_Management_Service.dtos.DisbursementResponse;
import com.machidior.Loan_Management_Service.service.DisbursementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/disbursements")
@RequiredArgsConstructor
public class DisbursementController {

    private final DisbursementService service;

    @PostMapping("/disburse/business-loan")
    public ResponseEntity<DisbursementResponse> disburseBusinessLoan(@RequestBody DisbursementRequest request){
        return ResponseEntity.ok(service.disburseBusinessLoan(request));
    }
}
