package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import com.machidior.Loan_Management_Service.model.BusinessLoan;
import com.machidior.Loan_Management_Service.service.BusinessLoanApplicationService;
import com.machidior.Loan_Management_Service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-application/business")
@RequiredArgsConstructor
public class BusinessLoanApplicationController {

    private final BusinessLoanApplicationService service;
    private final FileStorageService fileStorageService;

    @PostMapping("/save-application-details")
    public ResponseEntity<BusinessLoanApplicationResponse> applyBusinessLoan(@RequestBody ApplicationDetails request){
        return ResponseEntity.ok(service.applyBusinessLoan(request));
    }

    @PostMapping(path = "/save-business-details/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusinessLoanApplicationResponse> saveBusinessDetails(
            @PathVariable String applicationNumber,
            @RequestPart("bankStatement") MultipartFile bankStatement,
            @RequestPart("insuranceComprehensiveCover") MultipartFile insuranceComprehensiveCover,
            @RequestPart("tinCertificate") MultipartFile tinCertificate,
            @RequestPart("brelaCertificate") MultipartFile brelaCertificate,
            @RequestPart("businessLicense") MultipartFile businessLicense,
            @RequestPart("businessDetails") List<BusinessDetails> businessDetails) throws IOException {

        return ResponseEntity.ok(service.saveBusinessDetails(applicationNumber,bankStatement,insuranceComprehensiveCover,tinCertificate,brelaCertificate,businessLicense,businessDetails));
    }

    @PostMapping(path = "/save-business-collaterals/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusinessLoanApplicationResponse> saveBusinessCollaterals(
            @PathVariable String applicationNumber,
            @RequestPart("collaterals") List<BusinessLoanCollateralRequest> collaterals,
            @RequestPart("photos") List<MultipartFile> photos) throws IOException {
        return ResponseEntity.ok(service.saveBusinessCollaterals(applicationNumber,collaterals,photos));
    }

    @PostMapping(path = "/save-business-guarantor/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusinessLoanApplicationResponse> saveBusinessGuarantor(
            @PathVariable String applicationNumber,
            @RequestPart("passport") MultipartFile passport,
            @RequestPart("identificationCard") MultipartFile identificationCard,
            @RequestPart("guarantorRequest") BusinessLoanGuarantorRequest guarantorRequest) throws IOException {
        return ResponseEntity.ok(service.saveBusinessGuarantor(applicationNumber,passport,identificationCard,guarantorRequest));
    }

    @GetMapping("/apply/{applicationNumber}")
    public ResponseEntity<BusinessLoanApplicationResponse> createApplication(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.createApplication(applicationNumber));
    }

    @PostMapping("/approve/{applicationNumber}")
    public ResponseEntity<BusinessLoan> approveApplication(@PathVariable String applicationNumber, @RequestBody LoanApplicationApprovalRequest request){
        return ResponseEntity.ok(service.approveBusinessLoanApplication(applicationNumber,request));
    }

    @PostMapping("/reject/{applicationNumber}")
    public ResponseEntity<BusinessLoanApplicationResponse> rejectBusinessLoanApplication(@PathVariable String applicationNumber, @RequestBody String rejectionReason){
        return ResponseEntity.ok(service.rejectBusinessLoanApplication(applicationNumber,rejectionReason));
    }

    @PostMapping("/return/{applicationNumber}")
    public ResponseEntity<BusinessLoanApplicationResponse> returnBusinessLoanApplication(@PathVariable String applicationNumber, @RequestBody String reasonOfReturn){
        return ResponseEntity.ok(service.returnBusinessLoanApplication(applicationNumber,reasonOfReturn));
    }

    @GetMapping("/load-business-file/{fileName}")
    public ResponseEntity<Resource> loadBusinessFile(@PathVariable String fileName) throws MalformedURLException {
        return ResponseEntity.ok(fileStorageService.loadBusinessFile(fileName));
    }

    @GetMapping("/load-guarantor-file/{fileName}")
    public ResponseEntity<Resource> loadGuarantorFile(@PathVariable String fileName) throws MalformedURLException {
        return ResponseEntity.ok(fileStorageService.loadGuarantorFile(fileName));
    }

    @GetMapping("/load-collateral-file/{fileName}")
    public ResponseEntity<Resource> loadCollateralFile(@PathVariable String fileName) throws MalformedURLException {
        return ResponseEntity.ok(fileStorageService.loadCollateralFile(fileName));
    }

    @GetMapping("/load-job-file/{fileName}")
    public ResponseEntity<Resource> loadJobFile(@PathVariable String fileName) throws MalformedURLException {
        return ResponseEntity.ok(fileStorageService.loadJobFile(fileName));
    }

    @GetMapping("/{applicationNumber}")
    public ResponseEntity<BusinessLoanApplicationResponse> getApplicationByApplicationNumber(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.getApplicationByApplicationNumber(applicationNumber));
    }

    @DeleteMapping("/delete/{applicationNumber}")
    public ResponseEntity<?> deleteApplicationByApplicationNumber(@PathVariable String applicationNumber) throws IOException {
        service.deleteLoanApplication(applicationNumber);
        return ResponseEntity.ok().body("Loan application deleted successfully!");
    }

}
