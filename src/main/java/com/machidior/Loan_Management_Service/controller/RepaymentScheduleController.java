package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.model.RepaymentSchedule;
import com.machidior.Loan_Management_Service.service.RepaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repayment-schedule")
@RequiredArgsConstructor
public class RepaymentScheduleController {
    private final RepaymentScheduleService service;

    @GetMapping("/loan-id/{loanId}")
    public ResponseEntity<RepaymentSchedule> findByLoanId(@PathVariable String loanId){
        return ResponseEntity.ok(service.getScheduleByLoanId(loanId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RepaymentSchedule>> getAllSchedule(){
        return ResponseEntity.ok(service.getAllSchedules());
    }

    @DeleteMapping("/delete-by-loan-id/{loanId}")
    public ResponseEntity<?> deleteByLoanId(@PathVariable String loanId){
        service.deleteByLoanId(loanId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAll(){
        service.deleteAllSchedules();
        return ResponseEntity.noContent().build();
    }
}
