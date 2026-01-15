package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.request.EmploymentRequest;
import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.model.requirement.EmploymentDetails;
import com.machidior.Loan_Management_Service.service.EmploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/employments")
@RequiredArgsConstructor
public class EmploymentController {

    private final EmploymentService service;


    @PostMapping(path = "/save-job-details/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequirementSubmissionResponse> saveEmploymentDetails(
            @PathVariable String applicationNumber,
            @RequestPart("paySlip") MultipartFile paySlip,
            @RequestPart("insuranceComprehensiveCover") MultipartFile insuranceComprehensiveCover,
            @RequestPart("contract") MultipartFile contract,
            @RequestPart("payrollDeduction") MultipartFile payrollDeduction,
            @RequestPart("employmentDetails") EmploymentRequest request
    ) throws IOException {
        return ResponseEntity.ok(service.saveEmploymentDetails(applicationNumber,paySlip,insuranceComprehensiveCover,contract, payrollDeduction, request));
    }
}
