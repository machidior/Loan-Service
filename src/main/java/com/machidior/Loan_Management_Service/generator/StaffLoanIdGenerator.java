package com.machidior.Loan_Management_Service.generator;

import com.machidior.Loan_Management_Service.repo.LoanIdGeneratorSequenceRepository;
import com.machidior.Loan_Management_Service.util.LoanIdGeneratorSequence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class StaffLoanIdGenerator implements IdentifierGenerator {

    private static final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private LoanIdGeneratorSequenceRepository sequenceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String PREFIX = "STF";

    @Override
    @Transactional
    public Serializable generate(SharedSessionContractImplementor session, Object object) {

        lock.lock();
        try {
            String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
            String monthKey = PREFIX + datePart;  // unique per month and product

            // Fetch or initialize current sequence for this month
            Optional<LoanIdGeneratorSequence> seqOpt =
                    sequenceRepository.findById(monthKey);

            LoanIdGeneratorSequence seq;
            int nextSeq;

            if (seqOpt.isPresent()) {
                seq = seqOpt.get();
                nextSeq = seq.getSequence() + 1;
                seq.setSequence(nextSeq);
            } else {
                seq = new LoanIdGeneratorSequence(monthKey, 1);
                nextSeq = 1;
            }

            // Persist updated sequence
            sequenceRepository.save(seq);

            // Format sequence part (e.g. 001, 002, …)
            String seqPart = String.format("%03d", nextSeq);

            return monthKey + seqPart;
        } finally {
            lock.unlock();
        }
    }
}
