package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.request.EmploymentRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EmploymentService {

    @Transactional
    RequirementSubmissionResponse saveEmploymentDetails(
            String applicationNumber,
            MultipartFile paySlip,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile contract,
            MultipartFile payrollDeduction,
            EmploymentRequest request
    ) throws IOException;
}
