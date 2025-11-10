package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.util.ApplicationNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationNumberSequenceRepository extends JpaRepository<ApplicationNumberSequence, String> {
}
