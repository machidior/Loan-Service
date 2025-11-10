package com.machidior.Loan_Management_Service.generator;

import com.machidior.Loan_Management_Service.config.SpringContext;
import com.machidior.Loan_Management_Service.repo.ApplicationNumberSequenceRepository;
import com.machidior.Loan_Management_Service.util.ApplicationNumberSequence;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ApplicationNumberGenerator {

    public synchronized String generateApplicationNumber() {
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
