package com.machidior.Loan_Management_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.machidior.Loan_Management_Service.feign")
public class  LoanManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanManagementServiceApplication.class, args);
	}

}
