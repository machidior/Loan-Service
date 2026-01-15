package com.machidior.Loan_Management_Service.service;


import com.machidior.Loan_Management_Service.dtos.request.BusinessDetailsRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BusinessDetailsService {


    @Transactional
    RequirementSubmissionResponse saveBusinessDetails(
            String applicationNumber,
            MultipartFile cashFlowStatement,
            MultipartFile insuranceComprehensiveCover,
            MultipartFile tinCertificate,
            MultipartFile brelaCertificate,
            MultipartFile businessLicense,
            BusinessDetailsRequest request) throws IOException;
}
