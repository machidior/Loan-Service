package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralResponse;
import com.machidior.Loan_Management_Service.service.BusinessLoanCollateralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business-loans/collaterals")
@RequiredArgsConstructor
public class BusinessLoanCollateralController {

    private final BusinessLoanCollateralService collateralService;


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollateral(@PathVariable Long id) {
        collateralService.deleteCollateral(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/application-number/{applicationNumber}")
    public ResponseEntity<List<BusinessLoanCollateralResponse>> getApplicationCollaterals(@PathVariable String applicationNumber){
        return ResponseEntity.ok(collateralService.getApplicationCollaterals(applicationNumber));
    }
}
