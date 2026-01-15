package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.request.AgricultureApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.service.AgricultureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/agriculture")
@RequiredArgsConstructor
public class AgricultureController {

    private final AgricultureService service;

    @PostMapping("/save/{applicationNumber}")
    public ResponseEntity<RequirementSubmissionResponse> saveAgricultureDetails(
            String applicationNumber,
            AgricultureApplicationRequest agricultureDetailsRequest
    ) {

//        ToDo: Implement file uploads for agriculture details
//        MultipartFile farmOwnershipProof,
//        MultipartFile farmPhotographs,
        return ResponseEntity.ok(service.saveAgriculturalDetails(applicationNumber,agricultureDetailsRequest));
    }

}
