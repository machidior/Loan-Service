package com.machidior.Loan_Management_Service.controller;


import com.machidior.Loan_Management_Service.dtos.request.BusinessDetailsRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.service.BusinessDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/business-details")
@RequiredArgsConstructor
public class BusinessDetailsController {

    private final BusinessDetailsService service;

    @PostMapping(path = "/save-business-details/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequirementSubmissionResponse> saveBusinessDetails(
            @PathVariable String applicationNumber,
            @RequestPart("cashFlowStatement") MultipartFile cashFlowStatement,
            @RequestPart("insuranceComprehensiveCover") MultipartFile insuranceComprehensiveCover,
            @RequestPart("tinCertificate") MultipartFile tinCertificate,
            @RequestPart("brelaCertificate") MultipartFile brelaCertificate,
            @RequestPart("businessLicense") MultipartFile businessLicense,
            @RequestPart("businessDetails") BusinessDetailsRequest businessDetailsRequest) throws IOException {

        return ResponseEntity.ok(service.saveBusinessDetails(applicationNumber,cashFlowStatement,insuranceComprehensiveCover,tinCertificate,brelaCertificate,businessLicense,businessDetailsRequest));
    }

}
