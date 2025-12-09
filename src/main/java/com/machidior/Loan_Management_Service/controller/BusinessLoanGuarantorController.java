package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.BusinessLoanGuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.BusinessLoanGuarantorResponse;
import com.machidior.Loan_Management_Service.service.BusinessLoanGuarantorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business-loans/guarantors")
@RequiredArgsConstructor
public class BusinessLoanGuarantorController {

    private final BusinessLoanGuarantorService guarantorService;



    @PutMapping("/{guarantorId}/approve")
    public ResponseEntity<BusinessLoanGuarantorResponse> approveGuarantor(
            @PathVariable Long guarantorId
    ) {
        return ResponseEntity.ok(guarantorService.approveGuarantor(guarantorId));
    }

    @GetMapping("/application-number/{applicationNumber}")
    public ResponseEntity<BusinessLoanGuarantorResponse> getBusinessLoanGuarantor(@PathVariable String applicationNumber){
        return ResponseEntity.ok(guarantorService.getBusinessLoanGuarantor(applicationNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuarantor(@PathVariable Long id) {
        guarantorService.deleteGuarantor(id);
        return ResponseEntity.noContent().build();
    }
}
