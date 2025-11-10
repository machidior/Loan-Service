package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.model.LoanProductTerms;
import com.machidior.Loan_Management_Service.service.LoanProductTermsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-product-terms")
public class LoanProductTermsController {

    private final LoanProductTermsService service;

    public LoanProductTermsController(LoanProductTermsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LoanProductTerms> createTerms(@RequestBody LoanProductTerms terms) {
        return ResponseEntity.ok(service.createOrUpdateTerms(terms));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanProductTerms> updateTerms(@PathVariable Long id,
                                                        @RequestBody LoanProductTerms updatedTerms) {
        return ResponseEntity.ok(service.updateTerms(id, updatedTerms));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<LoanProductTerms> getTermsByProductType(@PathVariable LoanProductType type) {
        return ResponseEntity.ok(service.getTermsByProduct(type));
    }

    @GetMapping
    public ResponseEntity<List<LoanProductTerms>> getAllTerms() {
        return ResponseEntity.ok(service.getAllTerms());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerms(@PathVariable Long id) {
        service.deleteTerms(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/range")
    public ResponseEntity<List<LoanProductTerms>> getByRange(
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max) {
        return ResponseEntity.ok(service.findByAmountRange(min, max));
    }
}
