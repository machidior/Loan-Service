package com.machidior.Loan_Management_Service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LoanEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public LoanEventProducer(KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendLoanDisbursedEvent(LoanDisbursedEventDTO loanDTO) throws JsonProcessingException {
            String message = objectMapper.writeValueAsString(loanDTO);
            kafkaTemplate.send("loan-disbursed-topic", message);
            System.out.println("Sent loan disbursed event: " + loanDTO.getLoanId());

    }
}
