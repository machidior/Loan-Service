package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.request.EmploymentRequest;
import com.machidior.Loan_Management_Service.enums.EmploymentType;
import com.machidior.Loan_Management_Service.model.LoanApplication;
import com.machidior.Loan_Management_Service.model.requirement.EmploymentDetails;
import org.springframework.stereotype.Component;

@Component
public class EmploymentMapper {

    public EmploymentDetails toEntity(EmploymentRequest request, LoanApplication application) {
        return EmploymentDetails.builder()
                .employerName(request.getEmployerName())
                .employmentType(parseEmploymentType(request.getEmploymentType()))
                .employerAddress(request.getEmployerAddress())
                .employerContactNumber(request.getEmployerContactNumber())
                .jobTitle(request.getJobTitle())
                .monthlyIncome(request.getMonthlyIncome())
                .netMonthlyIncome(request.getNetMonthlyIncome())
                .loanApplication(application)
                .employmentStartDate(request.getEmploymentStartDate())
                .employmentDurationMonths(request.getEmploymentDurationMonths())
                .build();
    }

    private EmploymentType parseEmploymentType(String type) {
        try {
            return type != null ? EmploymentType.valueOf(type.toUpperCase()): null;
        } catch ( IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid employment type: " + type);
        }
    }
}
