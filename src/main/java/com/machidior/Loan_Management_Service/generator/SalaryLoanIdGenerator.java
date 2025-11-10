package com.machidior.Loan_Management_Service.generator;

import com.machidior.Loan_Management_Service.config.SpringContext;
import com.machidior.Loan_Management_Service.repo.LoanIdGeneratorSequenceRepository;
import com.machidior.Loan_Management_Service.util.LoanIdGeneratorSequence;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalaryLoanIdGenerator implements IdentifierGenerator {

    @Override
    public synchronized Serializable generate(SharedSessionContractImplementor session, Object object) {
        LoanIdGeneratorSequenceRepository sequenceRepository =
                SpringContext.getBean(LoanIdGeneratorSequenceRepository.class);

        String monthKey = LocalDate.now().format(DateTimeFormatter.ofPattern("MMyy"));
        LoanIdGeneratorSequence sequence = sequenceRepository.findById(monthKey)
                .orElse(new LoanIdGeneratorSequence(monthKey, 0));

        sequence.setSequence(sequence.getSequence() + 1);
        sequenceRepository.save(sequence);

        String formattedSequence = String.format("%03d", sequence.getSequence());
        String prefix = "SAL";
        return prefix + "-" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + formattedSequence;
    }
}
