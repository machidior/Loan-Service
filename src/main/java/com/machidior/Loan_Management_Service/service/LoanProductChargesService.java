package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.model.LoanProductCharges;
import com.machidior.Loan_Management_Service.repo.LoanProductChargesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanProductChargesService {

    private final LoanProductChargesRepository repository;

    public LoanProductChargesService(LoanProductChargesRepository repository) {
        this.repository = repository;
    }

    public LoanProductCharges createOrUpdateCharges(LoanProductCharges charges) {
        return repository.save(charges);
    }

    public LoanProductCharges getChargesByProductType(LoanProductType type) {
        return repository.findByProductType(type)
                .orElseThrow(() -> new RuntimeException("Charges not defined for product type: " + type));
    }

    public List<LoanProductCharges> getAllCharges() {
        return repository.findAll();
    }

    public void deleteCharges(Long id) {
        repository.deleteById(id);
    }
}
