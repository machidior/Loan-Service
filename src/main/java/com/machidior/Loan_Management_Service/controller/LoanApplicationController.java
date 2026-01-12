package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.request.*;
import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.model.EmploymentDetails;
import com.machidior.Loan_Management_Service.model.Loan;
import com.machidior.Loan_Management_Service.service.FileStorageService;
import com.machidior.Loan_Management_Service.service.LoanApplicationService;
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
@RequestMapping("/api/v1/loan-application")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService service;
    private final FileStorageService fileStorageService;

    @PostMapping("/create-application-details")
    public ResponseEntity<LoanApplicationResponse> createLoanApplicationDetails(@RequestBody ApplicationDetails request){
        return ResponseEntity.ok(service.createLoanApplicationDetails(request));
    }


    @PostMapping(path = "/save-collaterals/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequirementSubmissionResponse> saveCollaterals(
            @PathVariable String applicationNumber,
            @RequestPart("ownershipProof") List<MultipartFile> ownershipProof,
            @RequestPart("collaterals") List<CollateralRequest> collaterals,
            @RequestPart("photos") List<MultipartFile> photos,
            @RequestPart("insuranceDocuments") List<MultipartFile> insuranceDocuments
    ) throws IOException {
        return ResponseEntity.ok(service.saveCollaterals(applicationNumber,collaterals,photos, ownershipProof,insuranceDocuments));
    }

    @PostMapping(path = "/save-guarantor/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequirementSubmissionResponse> saveGuarantor(
            @PathVariable String applicationNumber,
            @RequestPart("passport") List<MultipartFile> passportPhotos,
            @RequestPart("identificationCard") List<MultipartFile> identificationCards,
            @RequestPart("guarantorConsents") List<MultipartFile> guarantorConsents,
            @RequestPart("incomeProofs") List<MultipartFile> incomeProofs,
            @RequestPart("guarantorRequest") List<GuarantorRequest> guarantorRequests
    ) throws IOException {
        return ResponseEntity.ok(service.saveGuarantor(applicationNumber,passportPhotos,identificationCards,guarantorConsents,incomeProofs,guarantorRequests));
    }

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

    @PostMapping(path = "/save-job-details/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LoanApplicationResponse> saveJobDetails(
            @PathVariable String applicationNumber,
            @RequestPart("bankStatement") MultipartFile bankStatement,
            @RequestPart("salarySlip") MultipartFile salarySlip,
            @RequestPart("insuranceComprehensiveCover") MultipartFile insuranceComprehensiveCover,
            @RequestPart("jobContract") MultipartFile jobContract,
            @RequestPart("jobDetails") EmploymentDetails employmentDetails
    ) throws IOException {
        return ResponseEntity.ok(service.saveJobDetails(applicationNumber,bankStatement,salarySlip,insuranceComprehensiveCover,jobContract, employmentDetails));
    }

    @PostMapping("/save-agriculture-details/{applicationNumber}")
    public ResponseEntity<RequirementSubmissionResponse> saveAgricultureDetails(
            String applicationNumber,
            AgricultureApplicationRequest agricultureDetailsRequest
    ) {

//        ToDo: Implement file uploads for agriculture details
//        MultipartFile farmOwnershipProof,
//        MultipartFile farmPhotographs,
        return ResponseEntity.ok(service.saveAgriculturalDetails(applicationNumber,agricultureDetailsRequest));
    }

    @GetMapping("/apply/{applicationNumber}")
    public ResponseEntity<LoanApplicationResponse> createApplication(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.applyLoan(applicationNumber));
    }

    @PostMapping("/approve/{applicationNumber}")
    public ResponseEntity<Loan> approveApplication(@PathVariable String applicationNumber, @RequestBody LoanApplicationApprovalRequest request){
        return ResponseEntity.ok(service.approveLoanApplication(applicationNumber,request));
    }

    @PostMapping("/reject/{applicationNumber}")
    public ResponseEntity<LoanApplicationResponse> rejectBusinessLoanApplication(@PathVariable String applicationNumber, @RequestBody String rejectionReason){
        return ResponseEntity.ok(service.rejectLoanApplication(applicationNumber,rejectionReason));
    }

    @PostMapping("/return/{applicationNumber}")
    public ResponseEntity<LoanApplicationResponse> returnBusinessLoanApplication(@PathVariable String applicationNumber, @RequestBody String reasonOfReturn){
        return ResponseEntity.ok(service.returnLoanApplication(applicationNumber,reasonOfReturn));
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
    public ResponseEntity<LoanApplicationResponse> getLoanApplication(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.getLoanApplication(applicationNumber));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LoanApplicationResponse>> getAllLoanApplications(){
        return ResponseEntity.ok(service.getAllLoanApplications());
    }

}
