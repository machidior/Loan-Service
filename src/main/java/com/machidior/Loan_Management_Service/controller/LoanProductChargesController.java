package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.model.LoanProductCharges;
import com.machidior.Loan_Management_Service.service.LoanProductChargesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-product-charges")
public class LoanProductChargesController {

    private final LoanProductChargesService service;

    public LoanProductChargesController(LoanProductChargesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LoanProductCharges> createOrUpdate(@RequestBody LoanProductCharges charges) {
        return ResponseEntity.ok(service.createOrUpdateCharges(charges));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<LoanProductCharges> getByProductType(@PathVariable LoanProductType type) {
        return ResponseEntity.ok(service.getChargesByProductType(type));
    }

    @GetMapping
    public ResponseEntity<List<LoanProductCharges>> getAll() {
        return ResponseEntity.ok(service.getAllCharges());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharges(@PathVariable Long id) {
        service.deleteCharges(id);
        return ResponseEntity.noContent().build();
    }
}
