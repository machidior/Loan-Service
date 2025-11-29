package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.ScheduleRequest;
import com.machidior.Loan_Management_Service.model.RepaymentScheduleItem;
import com.machidior.Loan_Management_Service.service.RepaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repayment-schedule")
@RequiredArgsConstructor
public class RepaymentScheduleController {
    private final RepaymentScheduleService service;

    @PostMapping("/flat")
    public ResponseEntity<List<RepaymentScheduleItem>> generateFlatRepaymentSchedule(@RequestBody ScheduleRequest request){
        return ResponseEntity.ok(service.generateFlatSchedule(request.getPrincipal(), request.getMonthlyRate(), request.getLoanFeeRate(), request.getTermMonths(), request.getDisbursementDate(), request.getFrequency()));
    }

    @PostMapping("/reducing")
    public ResponseEntity<List<RepaymentScheduleItem>> generateReducingRepaymentSchedule(@RequestBody ScheduleRequest request){
        return ResponseEntity.ok(service.generateReducingBalanceSchedule(request.getPrincipal(), request.getMonthlyRate(), request.getLoanFeeRate(), request.getTermMonths(), request.getDisbursementDate(), request.getFrequency()));
    }
}
