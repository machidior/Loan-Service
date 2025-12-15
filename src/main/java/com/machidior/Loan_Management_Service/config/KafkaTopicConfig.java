package com.machidior.Loan_Management_Service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic loanDisbursedTopic() {
        return new NewTopic("loan-disbursed-topic", 3, (short) 1);
    }

}
