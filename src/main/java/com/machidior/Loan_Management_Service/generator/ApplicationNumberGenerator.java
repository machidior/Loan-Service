package com.machidior.Loan_Management_Service.generator;

import com.machidior.Loan_Management_Service.config.SpringContext;
import com.machidior.Loan_Management_Service.repo.ApplicationNumberSequenceRepository;
import com.machidior.Loan_Management_Service.util.ApplicationNumberSequence;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ApplicationNumberGenerator implements IdentifierGenerator {


    @Override
    public synchronized Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        ApplicationNumberSequenceRepository repository =
                SpringContext.getBean(ApplicationNumberSequenceRepository.class);

        String dateKey = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        ApplicationNumberSequence sequence = repository.findById(dateKey)
                .orElse(new ApplicationNumberSequence(dateKey, 0));

        sequence.setSequence(sequence.getSequence() + 1);
        repository.save(sequence);

        String formattedSequence = String.format("%03d", sequence.getSequence());

        return "APP-" + dateKey + "-" + formattedSequence;
    }
}
