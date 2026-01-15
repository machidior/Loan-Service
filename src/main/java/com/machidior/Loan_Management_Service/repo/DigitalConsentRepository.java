package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.DigitalConsent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalConsentRepository extends JpaRepository<DigitalConsent, Long> {
}
