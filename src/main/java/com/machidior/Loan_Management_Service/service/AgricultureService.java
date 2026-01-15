package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.request.AgricultureApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AgricultureService {
    @Transactional
    RequirementSubmissionResponse saveAgriculturalDetails(
            String applicationNumber,
            AgricultureApplicationRequest request
    );
}
