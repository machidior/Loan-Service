package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> {
    Optional<RepaymentSchedule> findByLoanId(String loanId);
}
