package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.request.GuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.response.GuarantorResponse;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GuarantorService {
    @Transactional
    RequirementSubmissionResponse saveGuarantor(
            String applicationNumber,
            List<MultipartFile> passportPhotos,
            List<MultipartFile> identificationCards,
            List<MultipartFile> guarantorConsents,
            List<MultipartFile> incomeProofs,
            List<GuarantorRequest> guarantorRequests
    ) throws IOException;

    GuarantorResponse getLoanGuarantor(String applicationNumber);

    GuarantorResponse approveGuarantor(Long guarantorId);

    void deleteGuarantor(Long id);
}
