package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.model.LoanProductTerms;
import com.machidior.Loan_Management_Service.repo.LoanProductTermsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanProductTermsService {

    private final LoanProductTermsRepository repository;

    public LoanProductTermsService(LoanProductTermsRepository repository) {
        this.repository = repository;
    }

    public LoanProductTerms createOrUpdateTerms(LoanProductTerms terms) {
        validateTerms(terms);
        return repository.save(terms);
    }

    public LoanProductTerms updateTerms(Long id, LoanProductTerms updatedTerms) {
        LoanProductTerms existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terms not found with ID: " + id));

        existing.setMinAmount(updatedTerms.getMinAmount());
        existing.setMaxAmount(updatedTerms.getMaxAmount());
        existing.setMonthlyInterestRate(updatedTerms.getMonthlyInterestRate());
        existing.setMaximumTermMonths(updatedTerms.getMaximumTermMonths());
        existing.setExtraRules(updatedTerms.getExtraRules());
        existing.setMinGroupMembers(updatedTerms.getMinGroupMembers());
        existing.setMaxGroupMembers(updatedTerms.getMaxGroupMembers());

        validateTerms(existing);
        return repository.save(existing);
    }

    public LoanProductTerms getTermsByProduct(LoanProductType type) {
        return repository.findByProductType(type)
                .orElseThrow(() -> new RuntimeException("Loan product terms not found for type: " + type));
    }

    public List<LoanProductTerms> getAllTerms() {
        return repository.findAll();
    }

    public void deleteTerms(Long id) {
        repository.deleteById(id);
    }

    public List<LoanProductTerms> findByAmountRange(BigDecimal min, BigDecimal max) {
        return repository.findAll().stream()
                .filter(t -> (min == null || t.getMinAmount().compareTo(min) >= 0) &&
                        (max == null || t.getMaxAmount().compareTo(max) <= 0))
                .toList();
    }

    private void validateTerms(LoanProductTerms terms) {
        if (terms.getProductType() == null)
            throw new IllegalArgumentException("Product type is required.");

        if (terms.getProductType().name().equals("GROUP_PRODUCT")) {
            if (terms.getMinGroupMembers() == null || terms.getMaxGroupMembers() == null)
                throw new IllegalArgumentException("Group products must specify min and max group members.");
        }

        if (terms.getMinAmount() == null || terms.getMaxAmount() == null)
            throw new IllegalArgumentException("Amount range must be defined.");
    }
}
