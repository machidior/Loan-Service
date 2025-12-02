package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanCollateralResponse;
import com.machidior.Loan_Management_Service.service.BusinessLoanCollateralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business-loans/{loanId}/collaterals")
@RequiredArgsConstructor
public class BusinessLoanCollateralController {

    private final BusinessLoanCollateralService collateralService;

    @PostMapping
    public ResponseEntity<BusinessLoanCollateralResponse> addCollateral(
            @PathVariable String loanId,
            @RequestBody BusinessLoanCollateralRequest request
    ) {
        return ResponseEntity.ok(collateralService.addCollateral(loanId, request));
    }

//    @GetMapping
//    public ResponseEntity<List<BusinessLoanCollateralResponse>> getCollaterals(
//            @PathVariable String loanId
//    ) {
//        return ResponseEntity.ok(collateralService.getAllCollaterals(loanId));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollateral(@PathVariable Long id) {
        collateralService.deleteCollateral(id);
        return ResponseEntity.noContent().build();
    }
}
