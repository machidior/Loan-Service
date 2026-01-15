package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.request.CollateralRequest;
import com.machidior.Loan_Management_Service.dtos.response.CollateralResponse;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CollateralService {
    @Transactional
    RequirementSubmissionResponse saveCollaterals(
            String applicationNumber,
            List<CollateralRequest> collateralRequests,
            List<MultipartFile> photos,
            List<MultipartFile> ownershipProofs,
            List<MultipartFile> insuranceDocuments
    ) throws IOException;

    List<CollateralResponse> getLoanCollaterals(String applicationNumber);

    void deleteCollateral(Long id);
}
