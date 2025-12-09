package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.model.JobDetails;
import com.machidior.Loan_Management_Service.model.SalaryLoan;
import com.machidior.Loan_Management_Service.service.SalaryLoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-application/salary")
@RequiredArgsConstructor
public class SalaryLoanApplicationController {

    private final SalaryLoanApplicationService service;

    @PostMapping("/save-application-details")
    public ResponseEntity<SalaryLoanApplicationResponse> applySalaryLoan(@RequestBody ApplicationDetails details){
        return ResponseEntity.ok(service.applySalaryLoan(details));
    }

    @PostMapping(path = "/save-job-details/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalaryLoanApplicationResponse> saveJobDetails(
            @PathVariable String applicationNumber,
            @RequestPart("bankStatement") MultipartFile bankStatement,
            @RequestPart("salarySlip") MultipartFile salarySlip,
            @RequestPart("insuranceComprehensiveCover") MultipartFile insuranceComprehensiveCover,
            @RequestPart("jobContract") MultipartFile jobContract,
            @RequestPart("jobDetails") JobDetails jobDetails
    ) throws IOException {
        return ResponseEntity.ok(service.saveJobDetails(applicationNumber,bankStatement,salarySlip,insuranceComprehensiveCover,jobContract,jobDetails));
    }

    @PostMapping(path = "/save-loan-collaterals/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalaryLoanApplicationResponse> saveLoanCollaterals(
            @PathVariable String applicationNumber,
            @RequestPart("collaterals") List<SalaryLoanCollateralRequest> collaterals,
            @RequestPart("photos") List<MultipartFile> photos) throws IOException {
        return ResponseEntity.ok(service.saveLoanCollaterals(applicationNumber,collaterals,photos));
    }

    @PostMapping(path = "/save-loan-guarantor/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalaryLoanApplicationResponse> saveBusinessGuarantor(
            @PathVariable String applicationNumber,
            @RequestPart("passport") MultipartFile passport,
            @RequestPart("identificationCard") MultipartFile identificationCard,
            @RequestPart("guarantorRequest") SalaryLoanGuarantorRequest guarantorRequest) throws IOException {
        return ResponseEntity.ok(service.saveLoanGuarantor(applicationNumber,passport,identificationCard,guarantorRequest));
    }

    @GetMapping("/apply/{applicationNumber}")
    public ResponseEntity<SalaryLoanApplicationResponse> createApplication(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.createApplication(applicationNumber));
    }

    @PostMapping("/approve/{applicationNumber}")
    public ResponseEntity<SalaryLoan> approveApplication(@PathVariable String applicationNumber, @RequestBody LoanApplicationApprovalRequest request){
        return ResponseEntity.ok(service.approveSalaryLoanApplication(applicationNumber,request));
    }

    @PostMapping("/reject/{applicationNumber}")
    public ResponseEntity<SalaryLoanApplicationResponse> rejectSalaryLoanApplication(@PathVariable String applicationNumber, @RequestBody String rejectionReason){
        return ResponseEntity.ok(service.rejectSalaryLoanApplication(applicationNumber,rejectionReason));
    }

    @PostMapping("/return/{applicationNumber}")
    public ResponseEntity<SalaryLoanApplicationResponse> returnSalaryLoanApplication(@PathVariable String applicationNumber, @RequestBody String reasonOfReturn){
        return ResponseEntity.ok(service.returnSalaryLoanApplication(applicationNumber,reasonOfReturn));
    }
}
